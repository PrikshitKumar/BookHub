package com.prikshitkumar.bookhub.fragments

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.prikshitkumar.bookhub.R

class Profile : Fragment() {

    lateinit var devInstagram: Button
    lateinit var devFacebook: Button
    lateinit var devLinkedin: Button
    lateinit var devGithub: Button


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        devInstagram = view.findViewById(R.id.id_devInstagram)
        devFacebook = view.findViewById(R.id.id_devFacebook)
        devLinkedin = view.findViewById(R.id.id_devLinkedin)
        devGithub = view.findViewById(R.id.id_devGithub)

        devInstagram.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("http://www.instagram.com/_.dark_devil._71/"))
            startActivity(intent)
        }

        devFacebook.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://www.facebook.com/profile.php?id=100004882218930"))
            startActivity(intent)
        }

        devLinkedin.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://www.linkedin.com/in/prikshit-kumar-a6a53718b/"))
            startActivity(intent)
        }

        devGithub.setOnClickListener {
            val intent = Intent()
            intent.setAction(Intent.ACTION_VIEW)
            intent.addCategory(Intent.CATEGORY_BROWSABLE)
            intent.setData(Uri.parse("https://github.com/PrikshitKumar"))
            startActivity(intent)
        }

        return view
    }

}