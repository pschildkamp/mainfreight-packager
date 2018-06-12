package packing;

public class ContainerTooSmallException extends RuntimeException {
    ContainerTooSmallException(String msg) {
        super(msg);
    }
}
