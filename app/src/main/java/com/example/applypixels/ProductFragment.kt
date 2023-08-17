package com.example.applypixels

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast

class ProductFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        var rowView=inflater.inflate(R.layout.fragment_product, container, false)
        var button=rowView.findViewById<Button>(R.id.buyBtn)
        var button2=rowView.findViewById<Button>(R.id.butBtn2)
        var button3=rowView.findViewById<Button>(R.id.butBtn3)
        var button4=rowView.findViewById<Button>(R.id.butBtn4)
        var button5=rowView.findViewById<Button>(R.id.butBtn5)

        button.setOnClickListener {
            Toast.makeText(requireContext(),"Item added successfully", Toast.LENGTH_SHORT).show()
        }
        button2.setOnClickListener {
            Toast.makeText(requireContext(),"Item added successfully", Toast.LENGTH_SHORT).show()
        }
        button3.setOnClickListener {
            Toast.makeText(requireContext(),"Item added successfully", Toast.LENGTH_SHORT).show()
        }
        button4.setOnClickListener {
            Toast.makeText(requireContext(),"Item added successfully", Toast.LENGTH_SHORT).show()
        }
        button5.setOnClickListener {
            Toast.makeText(requireContext(),"Item added successfully", Toast.LENGTH_SHORT).show()
        }

        return rowView
    }
}