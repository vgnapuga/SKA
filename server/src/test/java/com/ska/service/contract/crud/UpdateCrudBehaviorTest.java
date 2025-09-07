package com.ska.service.contract.crud;


public interface UpdateCrudBehaviorTest extends GetCrudBehaviorTest {

    void shouldThrowException_whenNotFoundId();

}
