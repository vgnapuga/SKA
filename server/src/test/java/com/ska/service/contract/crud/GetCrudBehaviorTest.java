package com.ska.service.contract.crud;


public interface GetCrudBehaviorTest extends CreateCrudBehaviorTest {

    void shouldThrowException_whenNullId();

    void shouldThrowException_whenLessThanOneId(Long id);

}
