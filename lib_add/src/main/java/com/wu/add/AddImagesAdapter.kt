package com.wu.add

import android.content.Context
import android.util.DisplayMetrics
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.RelativeLayout
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.wkq.lib_base.adapter.AddImagesViewHolder
import com.wu.add.databinding.ItemAddImagesBinding

/**
 *
 * 作者:吴奎庆
 *
 * 时间:2021/12/30
 *
 * 用途:
 */


class AddImagesAdapter(
    mContext: Context,
    limtNum: Int,
    addImgs: Int,
    addCloseImgs: Int,
    addErrImgs: Int
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var mContext: Context
    var limtNum = 9
    var addImgs = -1
    var addCloseImgs = -1
    var addErrImgs = -1
    var addImages = ArrayList<AddImagesInfo>()

    init {
        this.mContext = mContext
        this.limtNum = limtNum
        this.addCloseImgs = addCloseImgs
        this.addImgs = addImgs
        this.addErrImgs = addErrImgs
    }

    var listener: OnAddClickListener? = null

    fun setOnAddListener(listener: OnAddClickListener) {
        this.listener = listener
    }

    fun getHeight(): Int {
        return (getScreenWidth(mContext) - dip2px(mContext, 80)) / 4
    }

    private fun dip2px(context: Context, dp: Int): Int {
        val density = context.resources.displayMetrics.density
        return (dp * density + 0.5).toInt()
    }

    private fun getScreenWidth(context: Context): Int {
        val wm = context
            .getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val outMetrics = DisplayMetrics()
        wm.defaultDisplay.getMetrics(outMetrics)
        return outMetrics.widthPixels
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        var viedataBinding: ItemAddImagesBinding =
            DataBindingUtil.inflate(
                LayoutInflater.from(mContext),
                R.layout.item_add_images,
                parent,
                false
            )

        var layout = RelativeLayout.LayoutParams(getHeight(), getHeight())
        layout.setMargins(10, 10, 10, 10)
        viedataBinding.root.layoutParams = layout

        var dataBindingViewHolder: AddImagesViewHolder = AddImagesViewHolder(viedataBinding.root)
        dataBindingViewHolder.setBinding(viewBinding = viedataBinding)
        return dataBindingViewHolder
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        var ktHolder = holder as? AddImagesViewHolder
        var binding = ktHolder!!.getBinding() as ItemAddImagesBinding
        ktHolder?.getBinding()?.executePendingBindings()
        if (addImgs!=-1){
            binding!!.ivAdd.setBackgroundResource(addImgs)
        }else{
            binding!!.ivAdd.setBackgroundResource(R.drawable.iv_add_images_add)
        }
        if (addCloseImgs!=-1){
            binding!!.ivClose.setBackgroundResource(addCloseImgs)
        }else{
            binding!!.ivClose.setBackgroundResource(R.drawable.iv_add_img_close)
        }


        binding.rlClose.setOnClickListener {
            processDelete(getItem(position))
        }

        binding.rlAdd.setOnClickListener {
            if (listener != null) listener!!.onAddClick(binding.rlAdd, getItem(position))
        }

        binding.ivContent.setOnClickListener {
            if (listener != null) listener!!.onAddClick(binding.ivContent, getItem(position))
        }

        if (getItem(position)!!.type.equals("2")) {
            binding.ivContent.visibility = View.VISIBLE
            binding.rlClose.visibility = View.VISIBLE
            binding.rlAdd.visibility = View.GONE
            if (addErrImgs!=-1){
             var requestOptions=   RequestOptions.centerCropTransform().error(addErrImgs)
                Glide.with(mContext).load(getItem(position)!!.imgUrl).apply(requestOptions).into(binding.ivContent)
            }else{
                var requestOptions=   RequestOptions.centerCropTransform().error(R.drawable.iv_add_err)
                Glide.with(mContext).load(getItem(position)!!.imgUrl).apply(requestOptions).into(binding.ivContent)
            }

        } else {
            binding.ivContent.visibility = View.GONE
            binding.rlAdd.visibility = View.VISIBLE
            binding.rlClose.visibility = View.GONE
        }

    }

    private fun processDelete(item: AddImagesInfo?) {
        addImages.remove(item)
        if (!hasAdd()) {
            addItem(AddImagesInfo(R.drawable.iv_add_images_add, "1"))
        } else {
            notifyDataSetChanged()
        }

    }

    fun addItems(lists: List<AddImagesInfo>) {
        processData(lists)
    }

    fun addItem(info: AddImagesInfo) {
        addImages.add(info)
        notifyDataSetChanged()
    }

    fun getItems(): ArrayList<AddImagesInfo>? {
        if (this.addImages == null) this.addImages = ArrayList()
        return this.addImages
    }

    private fun processData(addDatas: List<AddImagesInfo>) {
        if (addDatas == null) return

        var newList = processEnd(getItems()!!)
        if (newList.size >= limtNum) return
        if (newList!!.size + addDatas.size < limtNum) {
            newList!!.addAll(addDatas)
            newList!!.add(AddImagesInfo("", "1"))
        } else {
            newList!!.addAll(addDatas.subList(0, limtNum - newList.size))
        }
        notifyDataSetChanged()

    }

    private fun processEnd(addDatas: ArrayList<AddImagesInfo>): ArrayList<AddImagesInfo> {
        if (addDatas == null) return addDatas
        var iterator = addDatas.iterator()
        while (iterator.hasNext()) {
            var info = iterator.next()
            if (info.type.equals("1")) iterator.remove()
        }
        return addDatas
    }

    private fun hasAdd(): Boolean {
        getItems()!!.forEach {
            if (it.type.equals("1")) {
                return true
            }
        }
        return false
    }


    override fun getItemCount(): Int {
        return addImages.size
    }

    fun getItem(position: Int): AddImagesInfo? {
        if (addImages != null && position < addImages!!.size) {
            return this.addImages!!.get(position)
        }
        return null
    }

    interface OnAddClickListener {
        fun onAddClick(view: View?, item: AddImagesInfo?)
    }
}