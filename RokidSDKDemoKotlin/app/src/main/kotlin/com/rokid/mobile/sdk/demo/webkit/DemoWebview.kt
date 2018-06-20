package com.rokid.mobile.sdk.demo.webkit

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebView
import android.webkit.WebViewClient
import com.rokid.mobile.sdk.demo.SDKDemoActivity
import com.rokid.mobile.sdk.webkit.SDKWebview
import com.rokid.mobile.webview.lib.bean.TitleBarButton
import java.lang.ref.WeakReference

/**
 * Description: TODO
 * Author: Shper
 * Version: V0.1 2018/2/26
 */
class DemoWebview : SDKWebview {

    private var isMove: Boolean = false
    private var contextWeak: WeakReference<Context>? = null

    constructor(context: Context) : super(context) {
        init(context)
        initListener()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
        initListener()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
        initListener()
    }

    private fun init(context: Context) {
        contextWeak = WeakReference(context)

        settings.useWideViewPort = true
        settings.loadWithOverviewMode = true
        settings.domStorageEnabled = true
        settings.setAppCacheEnabled(true)
        settings.cacheMode = WebSettings.LOAD_DEFAULT
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            WebView.setWebContentsDebuggingEnabled(true)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            settings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }

        webChromeClient = DemoWebChromeClient(webBridge)
        webViewClient = WebViewClient()
    }

    private fun initListener() {
        setOnTouchListener(object : OnTouchListener {
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {

                when (event?.action) {
                    MotionEvent.ACTION_DOWN -> {
                        Log.d("DemoWebview", "ACTION_DOWN:" + isMove)
                        this@DemoWebview.requestDisallowInterceptTouchEvent(isMove)
                    }

                    MotionEvent.ACTION_MOVE -> {
                        Log.d("DemoWebview", "ACTION_MOVE:" + isMove)
                        this@DemoWebview.requestDisallowInterceptTouchEvent(isMove)
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        Log.d("DemoWebview", "ACTION_CANCEL:" + isMove)
                        this@DemoWebview.requestDisallowInterceptTouchEvent(isMove)
                    }

                    MotionEvent.ACTION_UP -> {
                        Log.d("DemoWebview", "ACTION_UP:" + isMove)

                        this@DemoWebview.requestDisallowInterceptTouchEvent(isMove)
                    }
                }

                return false
            }
        })
    }

    // here is BridgeModulePhoneDelegate
    override fun touchDown() {
        Log.d("DemoWebview", "touchDown:" + isMove)

        isMove = true
    }

    override fun touchMove() {
    }

    override fun touchUp() {
        Log.d("DemoWebview", "touchUp:" + isMove)

        isMove = false
    }

    // here is BridgeModuleAppDelegate
    override fun close() {
        //contextWeak?.get()?.finish()
    }

    override fun open(title: String, url: String) {
        this.loadUrl(url)
    }

    override fun openNewWebView(title: String, url: String) {
        if (null == contextWeak || null == contextWeak?.get()) {
            return
        }

        val intent = Intent(contextWeak!!.get(), DemoWebviewActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
        intent.putExtra(DemoWebviewActivity.KEY_TITLE, title)
        intent.putExtra(DemoWebviewActivity.KEY_URL, url)

        contextWeak!!.get()!!.startActivity(intent)
    }

    override fun openExternal(url: String) {
        val intent = Intent()
        intent.action = "android.intent.action.VIEW"
        val content_url = Uri.parse(url)
        intent.data = content_url

        contextWeak?.get()?.startActivity(intent)
    }

    override fun goBack(module: String, page: String) {
        if (module == "homebase" && page == "index") {
            val intent = Intent(contextWeak!!.get(), SDKDemoActivity::class.java)
            contextWeak!!.get()!!.startActivity(intent)
        }
    }

    // here is BridgeModuleViewDelegate
    override fun errorView(state: Boolean, retryUrl: String) {
    }

    override fun showLoading(message: String) {
    }

    override fun hideLoading() {
    }

    override fun setTitle(title: String) {
    }

    override fun setTitleBarRight(button: TitleBarButton) {
    }

    override fun setTitleBarRightDotState(state: Boolean) {
    }

    override fun setTitleBarStyle(style: String) {
    }

    override fun showToast(message: String) {
    }

}