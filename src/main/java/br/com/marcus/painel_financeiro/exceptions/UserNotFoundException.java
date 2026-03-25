package br.com.marcus.painel_financeiro.exceptions;

public class UserNotFoundException extends RuntimeException{
    public UserNotFoundException(){
        super("client not found");
    }

    public  UserNotFoundException(String msg) {
        super(msg);
    }
}
