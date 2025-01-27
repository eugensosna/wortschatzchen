package ua.sosna.wortschatz.wortschatztchen.storage;


public class StorageException extends RuntimeException {

	private static final long serialVersionUID = -7469441745134325057L;

	public StorageException(String message) {
		super(message);
	}

	public StorageException(String message, Throwable cause) {
		super(message, cause);
	}
}