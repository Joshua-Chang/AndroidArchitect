package org.devio.`as`.proj.common.flutter


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/12 1:30 AM
 * @创建人：常守达
 * @备注：
 */
interface IHiBridge<P, CallBack> {
    fun onBack(p: P?)
    fun goToNative(p: P)
    fun getHeaderParams(callBack: CallBack)
}