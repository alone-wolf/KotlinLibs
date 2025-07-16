package top.writerpass.jiebajvm

/**
 * Created by JackMeGo on 2017/7/5.
 */
interface RequestCallback<E> {
  fun onSuccess(result: E)
  fun onError(errorMsg: String?)
}
