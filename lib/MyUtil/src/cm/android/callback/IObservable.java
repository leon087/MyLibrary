package cm.android.callback;

/**
 * 监听器接口
 * 
 * @deprecated 不建议使用
 */
@Deprecated
public interface IObservable {
	void addObserver(Observer observer);

	void deleteObserver(Observer observer);

	void deleteObservers();

	void notifyObservers(Object data);
}
