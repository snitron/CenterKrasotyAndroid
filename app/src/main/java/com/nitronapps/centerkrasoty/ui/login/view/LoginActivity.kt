package com.nitronapps.centerkrasoty.ui.login.view

import android.animation.Animator
import android.content.Context
import android.content.Intent
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
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.yield
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

interface LoginView : MvpView {
    @StateStrategyType(AddToEndSingleStrategy::class)
    fun setStatus(status: LoginStatus)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun sayError(text: String)

    @StateStrategyType(AddToEndSingleStrategy::class)
    fun startMainActivity()
}

class LoginActivity : MvpAppCompatActivity(R.layout.activity_login), LoginView {
    private val presenter by moxyPresenter { LoginPresenter(this) }
    private val mask = MaskImpl(PredefinedSlots.RUS_PHONE_NUMBER, true)
    private val phoneWatcher = MaskFormatWatcher(mask)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setStatus(LoginStatus.LOGIN)

        buttonLogin.setOnClickListener {
            if (!editTextLogin.text.equals("") && !editTextPassword.equals("")) {
                it.visibility = View.GONE

                when (presenter.getCondition()) {

                    LoginStatus.LOGIN -> {
                        progressBarLogin.visibility = View.VISIBLE
                        presenter.loginClicked(
                            editTextLogin.text.toString(),
                            editTextPassword.text.toString()
                        )
                    }

                    LoginStatus.REGISTRATION -> {
                        if (!editTextName.text.equals("") && !editTextSurname.text.equals("") && editTextPhone.text.length == 18) {
                            progressBarLogin.visibility = View.VISIBLE
                            presenter.registrationClicked(
                                editTextLogin.text.toString(),
                                editTextPassword.text.toString(),
                                editTextName.text.toString(),
                                editTextSurname.text.toString(),
                                editTextPhone.text.toString()
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
                editTextPhone.visibility = View.GONE
                editTextPhone.visibility = View.GONE
                editTextName.visibility = View.GONE
                editTextSurname.visibility = View.GONE

                textViewRegistation.text = getString(R.string.registrationLabel)
                textViewRegistation.setOnClickListener {
                    presenter.setRegistration()
                }

                buttonLogin.text = getString(R.string.loginLabel)
            }

            LoginStatus.REGISTRATION -> {
                textViewTypeLabel.visibility = View.GONE
                editTextPhone.visibility = View.VISIBLE
                editTextName.visibility = View.VISIBLE
                editTextSurname.visibility = View.VISIBLE

                textViewRegistation.text = getString(R.string.loginLabel)
                textViewRegistation.setOnClickListener {
                    presenter.setLogin()
                }

                buttonLogin.text = getString(R.string.registrationLabel)
            }

            LoginStatus.SMS_VERIFICATION -> {
                progressBarLogin.visibility = View.GONE

                textViewTypeLabel.visibility = View.VISIBLE
                textViewRegistation.visibility = View.GONE

                editTextLogin.visibility = View.GONE
                editTextPassword.visibility = View.GONE
                editTextName.visibility = View.GONE
                editTextSurname.visibility = View.GONE

                editTextPhone.text = Editable.Factory.getInstance().newEditable("")

                val slots = UnderscoreDigitSlotsParser().parseSlots("_____")
                val watcher = MaskFormatWatcher(MaskImpl(slots, true))
                phoneWatcher.removeFromTextView()
                watcher.installOnAndFill(editTextPhone)
            }
        }

        buttonLogin.visibility = View.VISIBLE
    }

    override fun sayError(text: String) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show()

        progressBarLogin.visibility = View.GONE
        buttonLogin.visibility = View.VISIBLE
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
}

enum class LoginStatus {
    LOGIN, REGISTRATION, SMS_VERIFICATION;
}
