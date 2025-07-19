package ua.sosna.wortschatz.wortschatzchen.storage;

import java.io.Serial;


public class StorageException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = -7469441745134325057L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}