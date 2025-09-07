package com.ska.service.contract.crud;


public interface DeleteCrudBehavior {

    void shouldDeleteEntity_whenValidRequestData();

    void shouldThrowException_whenNullId();

    void shouldThrowException_whenNotFoundId();

    void shouldThrowException_whenLessThanOneId(Long userId);

}
