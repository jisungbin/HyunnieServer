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
import kotlinx.coroutines.flow.first


class SortDialog : BottomSheetDialogFragment() {

    companion object {
        private lateinit var bottomSheetDialogFragment: BottomSheetDialogFragment

        fun instance(): BottomSheetDialogFragment {
            if (!::bottomSheetDialogFragment.isInitialized) {
                bottomSheetDialogFragment = SortDialog()
            }
            return bottomSheetDialogFragment
        }
    }

    private val binding by lazy { LayoutSortDialogBinding.inflate(layoutInflater) }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = binding.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launchWhenCreated {
            when (DataManager.sortTypeFlow.first()) {
                Sort.FILE -> binding.tblType.setToggled(R.id.sort_file, true)
                Sort.FOLDER -> binding.tblType.setToggled(R.id.sort_folder, true)
            }

            when (DataManager.sortNameFlow.first()) {
                Sort.GANADA -> binding.tblName.setToggled(R.id.sort_ganada, true)
                Sort.DANAGA -> binding.tblName.setToggled(R.id.sort_danaga, true)
            }
        }

        @Throws(Exception::class)
        binding.tblName.onToggledListener = { _, toggle, _ ->
            lifecycleScope.launchWhenCreated {
                DataManager.setSortName(
                    when (toggle.id) {
                        R.id.sort_ganada -> Sort.GANADA
                        R.id.sort_danaga -> Sort.DANAGA
                        else -> throw Exception("unknown sort type")
                    }
                )
            }
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
}