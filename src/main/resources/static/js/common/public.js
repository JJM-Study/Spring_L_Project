const csrfToken = document.querySelector('meta[name=_csrf]').getAttribute('content');


// ID 유효성 검사 : 0 성공, 1 길이 문제, 2 특수문자 문제

const VALIDATION_STATUS = Object.freeze({
    SUCCESS: 0,
    EMPTY_INVALID: 1,
    LENGTH_INVALID: 2,
    FORMAT_INVALID: 3,
});

function isValidaUsername(username) {
const regex = /^[a-z0-9]+$/i;

    if (username === null || username === "") {
        return VALIDATION_STATUS.EMPTY_INVALID;
    } else if (username.length < 4 || username.length > 12) {
        return VALIDATION_STATUS.LENGTH_INVALID; // 아이디 자릿수 문제.
    } else if (!regex.test(username)) {
        return VALIDATION_STATUS.FORMAT_INVALID;
    } else {
        return VALIDATION_STATUS.SUCCESS;
    }
}