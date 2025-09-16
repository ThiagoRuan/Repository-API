package br.ufpb.dcx.dsc.repositorios.exception;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(
        @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss")
        LocalDateTime localDateTime,
        Integer code,
        String status,
        List<String> errors
) {
    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private LocalDateTime localDateTime;
        private Integer code;
        private String status;
        private List<String> errors;

        public Builder(){
            this.localDateTime = LocalDateTime.now();
        }

        public Builder localDateTime(LocalDateTime localDateTime) {
            this.localDateTime = localDateTime;
            return this;
        }

        public Builder code(Integer code) {
            this.code = code;
            return this;
        }
        public Builder status(String status) {
            this.status = status;
            return this;
        }
        public Builder errors(List<String> errors) {
            this.errors = errors;
            return this;
        }
        public Builder error(String error) {
            this.errors.add(error);
            return this;
        }
        public ErrorResponse build() {
            return new ErrorResponse(localDateTime, code, status, errors);
        }

    }
}
