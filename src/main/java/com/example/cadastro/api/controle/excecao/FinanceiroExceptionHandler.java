package com.example.cadastro.api.controle.excecao;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import com.example.cadastro.api.servico.excecao.PessoaInexistenteOuInativaException;

@ControllerAdvice
public class FinanceiroExceptionHandler extends ResponseEntityExceptionHandler {
	private final MessageSource messageSource;
	
	@Autowired
    public FinanceiroExceptionHandler(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

	@Override
    protected ResponseEntity<Object> handleHttpMessageNotReadable(HttpMessageNotReadableException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        String mensagem = this.messageSource.getMessage("parametro.invalido", null, LocaleContextHolder.getLocale());
        //String causa = ex.getCause().toString();
        String causa = ex.getCause() != null ? ex.getCause().toString() : ex.toString();
        
        return super.handleExceptionInternal (ex, new Erro(mensagem, causa), headers, status, request);
    }
	
	@Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers,
                                                                  HttpStatus status, WebRequest request) {

        List<Erro> erros = carregaListaDeErros(ex.getBindingResult());
        return super.handleExceptionInternal(ex, erros, headers, status, request);
    }
	
	@ExceptionHandler({EmptyResultDataAccessException.class})
    public ResponseEntity<Object> handleEmptyResultDataAccessException(EmptyResultDataAccessException ex, WebRequest request) {
        String mensagem = messageSource.getMessage("recurso.nao-encontrado", null, LocaleContextHolder.getLocale() );
        String  causa = ex.toString();

        return super.handleExceptionInternal(ex, new Erro(mensagem, causa), new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({IllegalArgumentException.class})
    public ResponseEntity<Object> handleIllegalArgumentException(IllegalArgumentException ex, WebRequest request) {
        String mensagem = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale() );
        String  causa = ex.toString();
        
        return super.handleExceptionInternal(ex, new Erro(mensagem, causa), new HttpHeaders(), HttpStatus.BAD_REQUEST, request );
    }
    
    @ExceptionHandler({DataIntegrityViolationException.class})
    public ResponseEntity<Object> handleDataIntegrityViolationException(DataIntegrityViolationException ex, WebRequest request) {
        String mensagem = messageSource.getMessage("recurso.operacao-nao-permitida", null, LocaleContextHolder.getLocale() );
        String  causa = ExceptionUtils.getRootCauseMessage(ex);
        
        return super.handleExceptionInternal(ex, new Erro(mensagem, causa), new HttpHeaders(), HttpStatus.BAD_REQUEST, request );
    }
    
    @ExceptionHandler({PessoaInexistenteOuInativaException.class})
    public ResponseEntity<Object> handlePessoaInexistenteOuInativaException(PessoaInexistenteOuInativaException ex) {
        String mensagem = messageSource.getMessage("pessoa.inexistente-ou-inativa", null, LocaleContextHolder.getLocale() );
        String  causa = ex.toString();
        
        return ResponseEntity.badRequest().body(new Erro(mensagem, causa));
    }
    
	private List<Erro> carregaListaDeErros(BindingResult bindingResult) {
        List<Erro> erros = new ArrayList<>();

        bindingResult.getFieldErrors().forEach(fieldError -> {
                    String mensagem = messageSource.getMessage(fieldError, LocaleContextHolder.getLocale());
                    erros.add(new Erro(mensagem ,fieldError.toString()));
                }
        );
        return erros;
    }
	
	private final class Erro {
        private final String mensagem;
        private final String causa;

        public Erro(String mensagem, String causa) {
            this.mensagem = mensagem;
            this.causa = causa;
        }

        public String getMensagem() {
            return mensagem;
        }

        public String getCausa() {
            return causa;
        }
    }
}
