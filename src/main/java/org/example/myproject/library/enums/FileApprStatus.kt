package org.example.myproject.library.enums


// 파일 승인 여부
enum class FileApprStatus {
    PENDING, // 허가 대기
    APPROVED, // 승인 완료
    REJECTED, // 반려 거부
    REVIEW // 재검토
}