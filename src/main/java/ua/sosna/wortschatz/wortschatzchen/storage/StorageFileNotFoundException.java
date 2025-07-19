package ua.sosna.wortschatz.wortschatzchen.storage;

import java.io.Serial;

public class StorageFileNotFoundException extends StorageException {

    @Serial
    private static final long serialVersionUID = 4056303644979585831L;

	public StorageFileNotFoundException(String message) {
		super(message);
	}

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}