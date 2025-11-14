const API_BASE_URL = 'http://localhost:8080/api/v1';

export async function setupSignup() {
  const form = document.querySelector('#signupForm');
  if (!form) return;

  form.addEventListener('submit', handleSignupSubmit);
}

async function handleSignupSubmit(event) {
  event.preventDefault();

  const email = document.querySelector('#email').value;
  const password = document.querySelector('#password').value;
  const nickname = document.querySelector('#nickname').value;

  // 입력값 검증
  if (!validateInputs(email, password, nickname)) {
    return;
  }

  // 에러 메시지 초기화
  clearErrorMessages();

  const signupData = {
    email,
    password,
    nickname,
  };

  try {
    const response = await fetch(`${API_BASE_URL}/members`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify(signupData),
    });

    if (!response.ok) {
      const error = await response.json();
      handleSignupError(error);
      return;
    }

    const result = await response.json();
    showSuccessMessage();
    resetForm();

    // 3초 후 홈페이지로 리다이렉트
    setTimeout(() => {
      window.location.href = '/';
    }, 3000);
  } catch (error) {
    console.error('회원 가입 요청 실패:', error);
    showErrorMessage('네트워크 오류가 발생했습니다. 다시 시도해주세요.');
  }
}

function validateInputs(email, password, nickname) {
  let isValid = true;

  // 이메일 검증
  const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
  if (!emailRegex.test(email)) {
    showFieldError('emailError', '올바른 이메일 형식을 입력해주세요');
    isValid = false;
  }

  // 비밀번호 검증 (최소 8자)
  if (password.length < 8) {
    showFieldError('passwordError', '비밀번호는 최소 8자 이상이어야 합니다');
    isValid = false;
  }

  // 닉네임 검증 (최소 2자)
  if (nickname.length < 2) {
    showFieldError('nicknameError', '닉네임은 최소 2자 이상이어야 합니다');
    isValid = false;
  }

  return isValid;
}

function handleSignupError(error) {
  const errorCode = error.errorCode;

  switch (errorCode) {
    case 'DUPLICATE_EMAIL':
      showFieldError('emailError', '이미 가입된 이메일입니다');
      break;
    case 'DUPLICATE_NICKNAME':
      showFieldError('nicknameError', '이미 사용 중인 닉네임입니다');
      break;
    default:
      showErrorMessage('회원 가입에 실패했습니다. 다시 시도해주세요.');
  }
}

function showFieldError(elementId, message) {
  const errorElement = document.querySelector(`#${elementId}`);
  if (errorElement) {
    errorElement.textContent = message;
    errorElement.style.display = 'block';
  }
}

function clearErrorMessages() {
  document.querySelectorAll('.error-message').forEach((el) => {
    el.textContent = '';
    el.style.display = 'none';
  });
}

function showSuccessMessage() {
  const successElement = document.querySelector('#successMessage');
  if (successElement) {
    successElement.style.display = 'block';
  }
}

function showErrorMessage(message) {
  alert(message);
}

function resetForm() {
  const form = document.querySelector('#signupForm');
  if (form) {
    form.reset();
  }
}
