package com.wu.demo

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.wu.add.AddImagesInfo
import com.wu.add.AddImagesView
import com.wu.demo.databinding.ActivityMainBinding
import com.wu.media.ImagePicker
import com.wu.media.PickerConfig
import com.wu.media.media.entity.Media

class MainActivity : AppCompatActivity() {

    var binding:ActivityMainBinding?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding= DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        binding!!.btAdd.setOnClickListener {
            ImagePicker.Builder()
                .setSelectGif(true)
                .maxNum(9)
                .needCamera(true)
                .selectMode(PickerConfig.PICKER_IMAGE)
                .builder() //跳转到图片选择页面 activity    请求码            结果码
                .start(this, 200, PickerConfig.DEFAULT_RESULT_CODE)
        }

        binding!!.addImages.setAddImagesViewListener(object : AddImagesView.AddImagesViewListener{
            override fun onAdd() {
                ImagePicker.Builder()
                    .setSelectGif(true)
                    .maxNum(9)
                    .needCamera(true)
                    .selectMode(PickerConfig.PICKER_IMAGE)
                    .builder() //跳转到图片选择页面 activity    请求码            结果码
                    .start(this@MainActivity, 200, PickerConfig.DEFAULT_RESULT_CODE)
            }

            override fun onPreview(item: AddImagesInfo?, lists: List<AddImagesInfo>?) {

            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode==PickerConfig.DEFAULT_RESULT_CODE){
            val select= data!!.getParcelableArrayListExtra<Media>(PickerConfig.EXTRA_RESULT)
            var adds=ArrayList<AddImagesInfo>()
            select!!.forEach {
                adds.add(AddImagesInfo(it.fileUri,"2"))
            }

            binding!!.addImages.addAddImages(adds)
        }

    }
}