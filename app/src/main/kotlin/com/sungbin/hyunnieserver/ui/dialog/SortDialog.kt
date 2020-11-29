package com.sungbin.hyunnieserver.ui.dialog


/**
 * Created by SungBin on 2020-09-11.
 */

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.sungbin.androidutils.util.Logger
import com.sungbin.hyunnieserver.R
import com.sungbin.hyunnieserver.databinding.LayoutSortDialogBinding


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
            when (toggle.id) {
                R.id.sort_ganada ->
            }
        }

        binding.tblType.onToggledListener = { _, toggle, _ ->
            Logger.w(arrayOf(toggle.id))
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