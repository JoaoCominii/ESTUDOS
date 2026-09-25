package com.rotaclara.exception;

/** Lançada quando uma operação viola uma das regras RN01–RN07. */
public class RegraDeNegocioException extends RuntimeException {
    public RegraDeNegocioException(String mensagem) {
        super(mensagem);
    }
}
