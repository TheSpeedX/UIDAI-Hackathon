package `in`.gov.uidai.auasample.settings

import `in`.gov.uidai.auasample.R
import `in`.gov.uidai.auasample.utils.Utils
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_language_change.*

class ChangeFaceRDLanguage : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View =
        inflater.inflate(R.layout.fragment_language_change, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
//        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.title = "Language Settings"

        when (Utils.LANGUAGE) {
            "en" -> selected1.visibility = View.VISIBLE
            "hi" -> selected2.visibility = View.VISIBLE
            "bn" -> selected3.visibility = View.VISIBLE
            "mr" -> selected4.visibility = View.VISIBLE
            "kn" -> selected5.visibility = View.VISIBLE
            "ml" -> selected6.visibility = View.VISIBLE
            "or" -> selected7.visibility = View.VISIBLE
            "ta" -> selected8.visibility = View.VISIBLE
            "te" -> selected9.visibility = View.VISIBLE
        }
        setClickListeners()
    }


    private fun setClickListeners() {

        lang1_eng1.setOnClickListener {
            changeLanguage("en")
            selected1.visibility = View.VISIBLE
            selected2.visibility = View.INVISIBLE
            selected3.visibility = View.INVISIBLE
            selected4.visibility = View.INVISIBLE
            selected5.visibility = View.INVISIBLE
            selected6.visibility = View.INVISIBLE
            selected7.visibility = View.INVISIBLE
            selected8.visibility = View.INVISIBLE
            selected9.visibility = View.INVISIBLE
        }

        lang2_hindi2.setOnClickListener {
            changeLanguage("hi")
            selected1.visibility = View.INVISIBLE
            selected2.visibility = View.VISIBLE
            selected3.visibility = View.INVISIBLE
            selected4.visibility = View.INVISIBLE
            selected5.visibility = View.INVISIBLE
            selected6.visibility = View.INVISIBLE
            selected7.visibility = View.INVISIBLE
            selected8.visibility = View.INVISIBLE
            selected9.visibility = View.INVISIBLE
        }
        lang3_bangla3.setOnClickListener {
            changeLanguage("bn")
            selected1.visibility = View.INVISIBLE
            selected2.visibility = View.INVISIBLE
            selected3.visibility = View.VISIBLE
            selected4.visibility = View.INVISIBLE
            selected5.visibility = View.INVISIBLE
            selected6.visibility = View.INVISIBLE
            selected7.visibility = View.INVISIBLE
            selected8.visibility = View.INVISIBLE
            selected9.visibility = View.INVISIBLE
        }
        lang4_marathi4.setOnClickListener {
            changeLanguage("mr")
            selected1.visibility = View.INVISIBLE
            selected2.visibility = View.INVISIBLE
            selected3.visibility = View.INVISIBLE
            selected4.visibility = View.VISIBLE
            selected5.visibility = View.INVISIBLE
            selected6.visibility = View.INVISIBLE
            selected7.visibility = View.INVISIBLE
            selected8.visibility = View.INVISIBLE
            selected9.visibility = View.INVISIBLE
        }
        lang5_kannada5.setOnClickListener {
            changeLanguage("kn")
            selected1.visibility = View.INVISIBLE
            selected2.visibility = View.INVISIBLE
            selected3.visibility = View.INVISIBLE
            selected4.visibility = View.INVISIBLE
            selected5.visibility = View.VISIBLE
            selected6.visibility = View.INVISIBLE
            selected7.visibility = View.INVISIBLE
            selected8.visibility = View.INVISIBLE
            selected9.visibility = View.INVISIBLE
        }
        lang6_malayalam6.setOnClickListener {
            changeLanguage("ml")
            selected1.visibility = View.INVISIBLE
            selected2.visibility = View.INVISIBLE
            selected3.visibility = View.INVISIBLE
            selected4.visibility = View.INVISIBLE
            selected5.visibility = View.INVISIBLE
            selected6.visibility = View.VISIBLE
            selected7.visibility = View.INVISIBLE
            selected8.visibility = View.INVISIBLE
            selected9.visibility = View.INVISIBLE
        }
        lang7_oriya7.setOnClickListener {
            changeLanguage("or")
            selected1.visibility = View.INVISIBLE
            selected2.visibility = View.INVISIBLE
            selected3.visibility = View.INVISIBLE
            selected4.visibility = View.INVISIBLE
            selected5.visibility = View.INVISIBLE
            selected6.visibility = View.INVISIBLE
            selected7.visibility = View.VISIBLE
            selected8.visibility = View.INVISIBLE
            selected9.visibility = View.INVISIBLE
        }
        lang8_tamil8.setOnClickListener {
            changeLanguage("ta")
            selected1.visibility = View.INVISIBLE
            selected2.visibility = View.INVISIBLE
            selected3.visibility = View.INVISIBLE
            selected4.visibility = View.INVISIBLE
            selected5.visibility = View.INVISIBLE
            selected6.visibility = View.INVISIBLE
            selected7.visibility = View.INVISIBLE
            selected8.visibility = View.VISIBLE
            selected9.visibility = View.INVISIBLE
        }
        lang9_telugu9.setOnClickListener {
            changeLanguage("te")
            selected1.visibility = View.INVISIBLE
            selected2.visibility = View.INVISIBLE
            selected3.visibility = View.INVISIBLE
            selected4.visibility = View.INVISIBLE
            selected5.visibility = View.INVISIBLE
            selected6.visibility = View.INVISIBLE
            selected7.visibility = View.INVISIBLE
            selected8.visibility = View.INVISIBLE
            selected9.visibility = View.VISIBLE
        }

    }

    private fun changeLanguage(s: String) {
        Utils.LANGUAGE = s
        val sharedPreferences =
            requireActivity().getSharedPreferences(
                getString(R.string.login_preference),
                Context.MODE_PRIVATE
            )
        sharedPreferences.edit().putString(getString(R.string.facerd_lang), s).apply()
//        dismiss()
    }

}