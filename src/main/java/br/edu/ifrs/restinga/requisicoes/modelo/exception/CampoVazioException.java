package br.edu.ifrs.restinga.requisicoes.modelo.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class CampoVazioException extends RuntimeException{

    public CampoVazioException(String msg) {
        super("Campo "+msg+" não pode estar vazio !");
    }
}
