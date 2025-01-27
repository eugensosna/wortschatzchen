package ua.sosna.wortschatz.wortschatztchen.storage;

public class StorageFileNotFoundException extends StorageException {

	private static final long serialVersionUID = 4056303644979585831L;

	public StorageFileNotFoundException(String message) {
		super(message);
	}

	public StorageFileNotFoundException(String message, Throwable cause) {
		super(message, cause);
	}
}