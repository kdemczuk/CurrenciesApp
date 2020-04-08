package com.example.currencies

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.currencies.ui.currencies.CurrenciesFragment
import dagger.android.AndroidInjection


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        AndroidInjection.inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction().let { transaction ->
                val newCurrentFragment =
                    supportFragmentManager.findFragmentByTag(CurrenciesFragment.TAG)
                        ?: CurrenciesFragment.newInstance(CurrenciesFragment.TAG)
                transaction.replace(R.id.fragment_container, newCurrentFragment, CurrenciesFragment.TAG)
                transaction.commit()
            }
        }

    }
}
