package io.github.hyungjun.backend.post;

import lombok.RequiredArgsConstructor;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;

    @Transactional
    public Post createPost(String title, String content, String author) {
        Post post = new Post(title, content, author);
        return postRepository.save(post);
    }

    @Transactional(readOnly = true)
    public Post getPost(Long id) {
        Post post = postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        // 매 조회마다 즉시 조회수 증가 - 병목 발생!
        increaseViewCountWithRetry(id);

        return post;
    }

    /**
     * 조회수 증가 로직 - 의도적으로 비효율적으로 구현
     * 1. 매 조회마다 별도의 트랜잭션으로 DB UPDATE
     * 2. 낙관적 락 사용으로 동시성 충돌 발생
     * 3. Thread.sleep으로 병목 증폭
     */
    @Transactional
    public void increaseViewCount(Long postId) {
        try {
            // 의도적인 지연 - 병목 증폭
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        Post post = postRepository.findByIdWithOptimisticLock(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found"));

        post.increaseViewCount();
        // JPA 더티 체킹으로 트랜잭션 커밋 시 자동 UPDATE

        try {
            // 추가 지연 - 트랜잭션 커밋 전 대기
            Thread.sleep(50);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    /**
     * 낙관적 락 실패 시 재시도 로직
     * 동시 접근이 많을수록 재시도가 증가하여 성능 저하
     */
    private void increaseViewCountWithRetry(Long postId) {
        int maxRetries = 3;
        int retryCount = 0;

        while (retryCount < maxRetries) {
            try {
                increaseViewCount(postId);
                return;
            } catch (ObjectOptimisticLockingFailureException e) {
                retryCount++;
                if (retryCount >= maxRetries) {
                    // 최대 재시도 횟수 초과 시 무시 (조회수 증가 실패)
                    return;
                }
                // 재시도 전 대기
                try {
                    Thread.sleep(100 * retryCount);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    return;
                }
            }
        }
    }
}
