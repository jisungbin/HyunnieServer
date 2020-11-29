package com.sungbin.hyunnieserver.ui.dialog


/**
 * Created by SungBin on 2020-09-11.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.databinding.LayoutSortDialogBinding
import com.sungbin.hyunnieserver.datastore.DataManager
import com.sungbin.hyunnieserver.datastore.Sort


class SortDialog : BottomSheetDialogFragment() {

    private lateinit var binding: LayoutSortDialogBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = LayoutSortDialogBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.tblName.onToggledListener = { _, toggle, _ ->
        }

        @Throws(Exception::class)
        binding.tblType.onToggledListener = { _, toggle, _ ->
            lifecycleScope.launchWhenCreated {
                DataManager.setSortType(
                    when (toggle.id) {
                        R.id.sort_folder -> Sort.FOLDER
                        R.id.sort_file -> Sort.FILE
                        else -> throw Exception("unknown sort type")
                    }
                )
            }
        }
    }

    companion object {
        private lateinit var bottomSheetDialogFragment: BottomSheetDialogFragment

        fun instance(): BottomSheetDialogFragment {
            if (!::bottomSheetDialogFragment.isInitialized) {
                bottomSheetDialogFragment = SortDialog()
            }
            return bottomSheetDialogFragment
        }
    }
}