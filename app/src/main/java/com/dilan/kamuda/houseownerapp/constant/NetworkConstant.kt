package com.dilan.kamuda.houseownerapp.constant

import com.dilan.kamuda.houseownerapp.common.model.mysecret.udevictory_url

object NetworkConstant {
    //const val BASE_URL = mockapi_url
    //const val BASE_URL = dongo_url
    //const val BASE_URL = route_url
    //const val BASE_URL = udesanvictory_url
    const val BASE_URL = udevictory_url

    const val ENDPOINT_MENU = "menu/all"
    const val ENDPOINT_MENU_UPDATE = "menu/update"
    const val ENDPOINT_MENU_ITEM_UPDATE = "menu/update/{id}"
    const val ENDPOINT_MENU_SAVE = "menu/save"

    const val ENDPOINT_GET_ORDERS_ALL = "order/house/all"
    const val ENDPOINT_GET_ORDER_BY_ID = "order/{orderId}"
    const val ENDPOINT_GET_ORDERS_BY_STATE = "order/house/all/{status}"
    const val ENDPOINT_PUT_ORDER = "order/update/status/{id}/{status}"

}