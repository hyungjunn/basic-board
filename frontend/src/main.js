import './style.css'
import './pages/signup.css'
import signupHtml from './pages/signup.html?raw'
import { setupSignup } from './pages/signup.js'

document.querySelector('#app').innerHTML = signupHtml
setupSignup()
