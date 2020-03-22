package com.nitronapps.centerkrasoty.ui.login.view

import android.animation.Animator
import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.EdgeEffect
import android.widget.Toast
import androidx.core.view.ViewCompat
import com.nitronapps.centerkrasoty.R
import com.nitronapps.centerkrasoty.ui.login.presenter.LoginPresenter
import com.nitronapps.centerkrasoty.ui.view.MainActivity
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.ticker
import moxy.MvpAppCompatActivity
import moxy.MvpView
import moxy.ktx.moxyPresenter
import moxy.viewstate.strategy.AddToEndSingleStrategy
import moxy.viewstate.strategy.SingleStateStrategy
import moxy.viewstate.strategy.StateStrategyType
import ru.tinkoff.decoro.MaskImpl
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser
import ru.tinkoff.decoro.slots.PredefinedSlots
import ru.tinkoff.decoro.slots.Slot
import ru.tinkoff.decoro.watchers.FormatWatcher
import ru.tinkoff.decoro.watchers.MaskFormatWatcher
import kotlin.concurrent.timer

interface LoginView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setStatus(status: LoginStatus)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startMainActivity()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setProgressBarEnabled(by: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setButtonEnabled(by: Boolean)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startTimeCoroutine()

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun cancelTimeCoroutine()
}

class LoginActivity : MvpAppCompatActivity(R.layout.activity_login), LoginView {
    private val presenter by moxyPresenter { LoginPresenter(this) }
    private val mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)
    private val phoneWatcher = MaskFormatWatcher(mask)
    private val slots = UnderscoreDigitSlotsParser().parseSlots("_____")
    private val codeWatcher = MaskFormatWatcher(MaskImpl(slots, true))
    private var currentJob: Job? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setStatus(LoginStatus.LOGIN)

        buttonLogin.setOnClickListener {
            if (!editTextPhone.text.equals("") && !editTextPassword.text.equals("")) {
                it.visibility = View.GONE

                when (presenter.getCondition()) {

                    LoginStatus.LOGIN -> {
                        progressBarLogin.visibility = View.VISIBLE
                        presenter.loginClicked(
                            editTextPhone.text.toString(),
                            editTextPassword.text.toString()
                        )
                    }

                    LoginStatus.REGISTRATION -> {
                        if (!editTextName.text.equals("") && !editTextSurname.text.equals("") && editTextPhone.text.length == 18) {
                            progressBarLogin.visibility = View.VISIBLE
                            presenter.registrationClicked(
                                editTextPhone.text.toString(),
                                editTextPassword.text.toString(),
                                editTextName.text.toString(),
                                editTextSurname.text.toString()
                            )
                        } else
                            sayError(getString(R.string.fillAllFields))
                    }

                    LoginStatus.SMS_VERIFICATION -> {
                        if (!editTextPhone.equals("")) {
                            progressBarLogin.visibility = View.VISIBLE
                            presenter.smsClicked(editTextPhone.text.toString())
                        }
                    }

                }
            } else
                sayError(getString(R.string.fillAllFields))
        }

        phoneWatcher.installOnAndFill(editTextPhone)
    }

    override fun setStatus(status: LoginStatus) {
        when (status) {
            LoginStatus.LOGIN -> {
                textViewTypeLabel.visibility = View.GONE
                textViewLoginBack.visibility = View.GONE

                editTextPhone.visibility = View.VISIBLE
                editTextName.visibility = View.GONE
                editTextSurname.visibility = View.GONE

                editTextPhone.text = Editable.Factory().newEditable("")
                editTextName.text = Editable.Factory().newEditable("")
                editTextSurname.text = Editable.Factory().newEditable("")
                editTextPassword.text = Editable.Factory().newEditable("")

                textViewRegistation.text = getString(R.string.registrationLabel)
                textViewRegistation.setOnClickListener {
                    presenter.setRegistration()
                }

                buttonLogin.text = getString(R.string.loginLabel)
            }

            LoginStatus.REGISTRATION -> {
                textViewTypeLabel.visibility = View.GONE
                textViewLoginBack.visibility = View.GONE

                editTextPhone.visibility = View.VISIBLE
                editTextName.visibility = View.VISIBLE
                editTextSurname.visibility = View.VISIBLE
                editTextPassword.visibility = View.VISIBLE

                editTextPhone.text = Editable.Factory().newEditable("")
                editTextName.text = Editable.Factory().newEditable("")
                editTextSurname.text = Editable.Factory().newEditable("")
                editTextPassword.text = Editable.Factory().newEditable("")

                editTextPhone.text = Editable.Factory.getInstance().newEditable("")
                editTextPhone.textAlignment = View.TEXT_ALIGNMENT_TEXT_START

                codeWatcher.removeFromTextView()
                phoneWatcher.installOnAndFill(editTextPhone)

                textViewRegistation.text = getString(R.string.loginLabel)
                textViewRegistation.setOnClickListener {
                    presenter.setLogin()
                }

                buttonLogin.text = getString(R.string.registrationLabel)
            }

            LoginStatus.SMS_VERIFICATION -> {
                progressBarLogin.visibility = View.GONE

                textViewTypeLabel.visibility = View.VISIBLE
                textViewRegistation.visibility = View.VISIBLE
                textViewRegistation.setOnClickListener(null)
                textViewRegistation.setTextColor(Color.GRAY)

                textViewLoginBack.visibility = View.INVISIBLE
                textViewLoginBack.setOnClickListener {
                    presenter.cancelSmsPage()
                }

                startTimeCoroutine()

                editTextPassword.visibility = View.INVISIBLE
                editTextName.visibility = View.INVISIBLE
                editTextSurname.visibility = View.GONE

                editTextPhone.text = Editable.Factory.getInstance().newEditable("")
                editTextPhone.textAlignment = View.TEXT_ALIGNMENT_CENTER

                phoneWatcher.removeFromTextView()
                codeWatcher.installOnAndFill(editTextPhone)
            }
        }

        buttonLogin.visibility = View.VISIBLE
    }

    override fun sayError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

        progressBarLogin.visibility = View.GONE
        buttonLogin.visibility = View.VISIBLE
        editTextPhone.text = Editable.Factory().newEditable("")
    }

    override fun startMainActivity() {
        runOnUiThread {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    override fun onDestroy() {
        presenter.onDestroyCalled()
        super.onDestroy()
    }

    override fun onBackPressed() {}

    override fun setProgressBarEnabled(by: Boolean) {
        runOnUiThread {
            progressBarLogin.visibility = if (by) View.VISIBLE else View.GONE
        }
    }

    override fun setButtonEnabled(by: Boolean) {
        runOnUiThread {
            buttonLogin.isEnabled = by
        }
    }

    override  fun startTimeCoroutine() {
        textViewRegistation.setTextColor(Color.GRAY)
        currentJob = CoroutineScope(Dispatchers.IO).launch {
            for(i in 0..59){
                printSeconds(i)
                delay(1000)
            }

            launch(Dispatchers.Main) {
                textViewRegistation.text = getString(R.string.getNewSms)
                textViewRegistation.setTextColor(Color.BLACK)

                textViewRegistation.setOnClickListener{
                    presenter.requestNewSms()

                    textViewRegistation.setOnClickListener(null)
                }
            }
        }
    }

    private fun printSeconds(seconds: Int){
        CoroutineScope(Dispatchers.Main).launch {
            textViewRegistation.text = getString(R.string.getNewSms)
                .plus(" (")
                .plus(getString(R.string.through))
                .plus(" ")
                .plus(60 - seconds)
                .plus(" ")
                .plus(getString(R.string.sec))
                .plus(")")
        }
    }

    override fun cancelTimeCoroutine() {
        if(currentJob != null)
            currentJob!!.cancel()
    }
}

enum class LoginStatus {
    LOGIN, REGISTRATION, SMS_VERIFICATION;
}
