package com.wu.add

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import com.wu.add.databinding.LayoutAddImagesBinding

/**
 *
 * 作者:吴奎庆
 *
 * 时间:2021/12/30
 *
 * 用途:
 */


class AddImagesView @JvmOverloads constructor(
    mContext: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(mContext, attrs, defStyleAttr) {
    private var mContext: Context
    // 添加的图片资源
    private var addImgs = -1
    //关闭的图片资源
    private var addCloseImgs = -1
    //错误页的图片资源
    private var addErrImgs = -1
    //
    private var columnNums = 5
    private var add_limit_nums = 9
    private var mAdapter: AddImagesAdapter? = null


    init {
        this.mContext = mContext
        val tapeArray = mContext.obtainStyledAttributes(attrs, R.styleable.AddImagesStyle)
        addImgs = tapeArray.getResourceId(R.styleable.AddImagesStyle_add_imgs, -1)
        addCloseImgs = tapeArray.getResourceId(R.styleable.AddImagesStyle_add_close_imgs, -1)
        addErrImgs = tapeArray.getResourceId(R.styleable.AddImagesStyle_add_err_imgs, -1)
        columnNums = tapeArray.getInt(R.styleable.AddImagesStyle_column_nums, 5)
        add_limit_nums = tapeArray.getInt(R.styleable.AddImagesStyle_add_limit_nums, 9)
        initView()
    }

    var listener: AddImagesViewListener? = null

    fun setAddImagesViewListener(listener: AddImagesViewListener) {
        this.listener = listener
    }


    private fun initView() {
        var binding = DataBindingUtil.inflate<LayoutAddImagesBinding>(
            LayoutInflater.from(mContext),
            R.layout.layout_add_images,
            this,
            false
        )
        addView(binding.root)
        binding.rvContent.layoutManager = GridLayoutManager(mContext, columnNums)
        mAdapter = AddImagesAdapter(mContext, add_limit_nums,addImgs,addCloseImgs)
        binding.rvContent.adapter = mAdapter
        mAdapter!!.addItem(AddImagesInfo("", "1"))

        mAdapter!!.setOnAddListener(object : AddImagesAdapter.OnAddClickListener {
            override fun onAddClick(view: View?, item: AddImagesInfo?) {
                when (view!!.id) {
                    R.id.rl_add -> {
                        if (listener!=null)
                        listener!!.onAdd()
                    }
                    R.id.iv_content -> {
                        if (listener!=null)
                        listener!!.onPreview(item,mAdapter!!.getItems())
                    }
                }
            }

        })
    }

    fun addAddImages(lists: List<AddImagesInfo>) {
        mAdapter!!.addItems(lists)
    }


    interface AddImagesViewListener {
        fun onAdd()
        fun onPreview(item:AddImagesInfo?,lists: List<AddImagesInfo>?)
    }

}