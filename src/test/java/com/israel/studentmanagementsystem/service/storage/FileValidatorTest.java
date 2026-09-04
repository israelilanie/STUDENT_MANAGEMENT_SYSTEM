package com.israel.studentmanagementsystem.service.storage;

import org.junit.jupiter.api.Test;
import org.springframework.mock.web.MockMultipartFile;

import static org.assertj.core.api.Assertions.assertThatCode;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FileValidatorTest {
    private final FileValidator validator = new FileValidator();

    @Test
    void acceptsSupportedNonEmptyImage() {
        assertThatCode(() -> validator.validateAvatar(
                new MockMultipartFile("avatar", "avatar.png", "image/png", new byte[]{1})))
                .doesNotThrowAnyException();
    }

    @Test
    void rejectsMissingEmptyOversizedAndUnsupportedFiles() {
        assertThatThrownBy(() -> validator.validateAvatar(null)).hasMessage("File cannot be empty");
        assertThatThrownBy(() -> validator.validateAvatar(
                new MockMultipartFile("avatar", new byte[0]))).hasMessage("File cannot be empty");
        assertThatThrownBy(() -> validator.validateAvatar(
                new MockMultipartFile("avatar", "large.png", "image/png", new byte[5 * 1024 * 1024 + 1])))
                .hasMessageContaining("exceeds 5MB");
        assertThatThrownBy(() -> validator.validateAvatar(
                new MockMultipartFile("avatar", "document.pdf", "application/pdf", new byte[]{1})))
                .hasMessageContaining("Invalid file type");
    }
}
