package com.dirror.music.ui.activity

import android.content.Intent
import com.dirror.music.R
import com.dirror.music.ui.base.BaseActivity
import kotlinx.android.synthetic.main.activity_login.*

class LoginActivity : BaseActivity(R.layout.activity_login) {

    override fun initData() {

    }

    override fun initView() {
        // 白色状态栏图标
        // setStatusBarIconColor(this, false)
    }

    override fun initListener() {

        btnLoginByPhone.setOnClickListener {
            startActivity(Intent(this, LoginByPhoneActivity::class.java))
            send()
        }

        btnLoginByUid.setOnClickListener {
            startActivity(Intent(this, LoginByUidActivity::class.java))
            send()
        }

    }

    private fun send() {
        val intent = Intent()
        intent.putExtra("boolean_user", true)
        setResult(RESULT_OK, intent)
        finish()
    }

}