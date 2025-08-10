package com.ska.vo.user;

import com.ska.vo.ValueObject;


public final class Email extends ValueObject<String> {

    public Email(String value) {
        super(value);
    }


    @Override
    public final void checkValidation(String value) {
        validateNotBlank(value);

        String emailRegex = "^[\\w-.]+@[\\w-]+\\.[a-zA-Z]{2,}$";
        if (!value.matches(emailRegex))
            throw new IllegalArgumentException("Invalid email format");
    }
    
    @Override
    public final String toString() {
        return "Email{" +
            "value=" + this.value +
            "}";
    }

}
