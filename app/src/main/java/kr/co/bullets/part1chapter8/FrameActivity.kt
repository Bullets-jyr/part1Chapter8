package kr.co.bullets.part1chapter8

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kr.co.bullets.part1chapter8.databinding.ActivityFrameBinding

class FrameActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFrameBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFrameBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val images = (intent.getStringArrayExtra("images") ?: emptyArray()).map { uriString -> FrameItem(Uri.parse(uriString)) }

        val frameAdapter = FrameAdapter(images)

        binding.viewPager.adapter = frameAdapter
    }
}