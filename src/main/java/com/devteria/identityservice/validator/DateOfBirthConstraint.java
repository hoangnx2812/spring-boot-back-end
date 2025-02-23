package com.devteria.identityservice.validator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

//@Repeatable() Cho phép chúng ta sử dụng nhiều lần
//@Documented Cho phép chúng ta tạo javadoc từ anotation
@Target({FIELD})  // Nơi mà anotation này có thể được sử dụng
@Retention(RUNTIME) // Thời điểm mà anotation này sẽ được sử dụng
@Constraint(validatedBy = {DateOfBirthValidator.class}) // Class sẽ xử lý validate
public @interface DateOfBirthConstraint {

    String message() default "Invalid date of birth";  // Thông báo lỗi

    Class<?>[] groups() default { };  // Nhóm các ràng buộc

    Class<? extends Payload>[] payload() default { };  // Dữ liệu cần thiết để xác định cách xử lý validate

    int min();
}
