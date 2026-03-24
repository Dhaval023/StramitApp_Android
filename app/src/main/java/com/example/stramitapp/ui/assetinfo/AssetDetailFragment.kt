package com.example.stramitapp.ui.assetinfo

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.stramitapp.R
import com.example.stramitapp.databinding.FragmentAssetDetailBinding
import com.example.stramitapp.ui.search_asset.SearchResultItem

class AssetDetailFragment : Fragment() {

    private var _binding: FragmentAssetDetailBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_ASSET = "asset_item"

        fun newInstance(item: SearchResultItem): AssetDetailFragment {
            val fragment = AssetDetailFragment()
            val args = Bundle()
            args.putSerializable(ARG_ASSET, item)
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAssetDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val item = arguments?.getSerializable(ARG_ASSET) as? SearchResultItem ?: return

        populateFields(item)
        setupAntennaButton(item)
    }

    private fun setupAntennaButton(item: SearchResultItem) {
        binding.root.findViewById<ImageView>(R.id.antennaButton).setOnClickListener {
            val tag = item.tag?.trim()

            if (tag.isNullOrEmpty()) return@setOnClickListener

            findNavController().navigate(
                R.id.action_nav_asset_detail_to_nav_locate_tag_rfid,
                bundleOf(
                    "arg_tag" to tag,
                    "arg_barcode" to (item.barcode ?: "")
                )
            )
        }
    }
    private fun populateFields(a: SearchResultItem) {

        fun String?.orDash(): String = if (isNullOrBlank()) "-" else this.trim()

        fun combined(value: String?, uom: String?): String {
            val v = value?.trim().orEmpty()
            val u = uom?.trim().orEmpty()
            return when {
                v.isNotEmpty() && u.isNotEmpty() -> "$v $u"
                v.isNotEmpty() -> v
                else -> "-"
            }
        }

        with(binding) {
            locationValue.text                 = a.locationName.orDash()
            barcodeValue.text                  = a.barcode.orDash()
            productSkuValue.text               = a.companyAssetId.orDash()
            titleValue.text                    = a.title.orDash()
            splitNumberValue.text              = a.custom8.orDash()
            packDescriptionValue.text          = a.custom35.orDash()
            m3CoValue.text                     = a.custom13.orDash()
            deliveryNumberValue.text           = a.custom16.orDash()
            customerNameValue.text             = a.custom14.orDash()
            shipmentNumberValue.text           = a.custom18.orDash()
            departureDateValue.text            = a.purchaseDate.orDash()
            routeValue.text                    = a.custom19.orDash()
            addressValue.text                  = a.custom21.orDash()
            dropNumberValue.text               = a.custom17.orDash()
            deliveryInstructionValue.text      = a.custom20.orDash()
            supplierNumberValue.text           = a.custom10.orDash()
            supplierReferenceValue.text        = a.custom9.orDash()
            poNumberValue.text                 = a.custom12.orDash()
            girthValue.text                    = combined(a.custom3, a.custom29)
            numberOfBendsValue.text            = a.custom5.orDash()
            receivedDateValue.text             = a.createDate.orDash()
            weightValue.text                   = combined(a.weight, a.custom28)
            lengthValue.text                   = combined(a.custom1, a.custom25)
            colourValue.text                   = combined(a.custom4, a.custom30)
            heightValue.text                   = combined(a.weightUom, a.custom26)
            widthValue.text                    = combined(a.custom2, a.custom27)
            quantityValue.text                 = a.assetValue.orDash()
            packNumberValue.text               = a.custom6.orDash()
            totalPackNumberValue.text          = a.custom7.orDash()
            docketNumberValue.text             = a.custom11.orDash()
            customerReferenceValue.text        = a.custom15.orDash()
            m3DoValue.text                     = a.custom22.orDash()
            packageStructureValue.text         = a.custom31.orDash()
            manufacturingInstructionValue.text = a.custom32.orDash()
            scheduleNumberValue.text           = a.custom33.orDash()
            markNumberValue.text               = a.custom34.orDash()
            packageNumberValue.text            = a.custom36.orDash()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}