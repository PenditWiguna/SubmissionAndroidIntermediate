package android.me.keenanstory

import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.me.keenanstory.databinding.ActivityDetailBinding
import com.bumptech.glide.Glide

class DetailActivity : AppCompatActivity() {
    private lateinit var binding: ActivityDetailBinding
    companion object {
        const val EXTRA_STORY = "extra_story"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val story = getParceableData()
        if (story != null) {
            setStoryDetail(story)
        }
    }

    private fun setStoryDetail(storyData: StoryData) {
        Glide.with(this@DetailActivity).load(storyData.avtUrl).into(binding.ivUserPhoto)
        binding.tvUserName.text = storyData.userName
        binding.tvUserDesc.text = storyData.desc
    }
    private fun getParceableData(): StoryData? {
        if (Build.VERSION.SDK_INT >= 33) {
            return intent.getParcelableExtra(EXTRA_STORY, StoryData::class.java)
        } else {
            @Suppress("DEPRECATED")
            return intent.getParcelableExtra(EXTRA_STORY)
        }
    }
}