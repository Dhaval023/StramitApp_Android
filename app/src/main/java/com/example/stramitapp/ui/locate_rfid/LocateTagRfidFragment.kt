package com.example.stramitapp.ui.locate_rfid

import android.graphics.RectF
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.stramitapp.MainActivity
import com.example.stramitapp.R

class LocateTagRfidFragment : Fragment() {

    private val TAG = "LocateTagRfidFrag"
    private val viewModel: LocateTagRFIDViewModel by viewModels()
    private lateinit var tagPatternLabel: TextView
    private lateinit var relativeDistanceLabel: TextView
    private lateinit var readerConnectionLabel: TextView
    private lateinit var distanceBar: View
    private lateinit var distanceBarContainer: View
    private lateinit var hintLayout: View

    companion object {
        const val ARG_TAG     = "arg_tag"
        const val ARG_BARCODE = "arg_barcode"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = inflater.inflate(R.layout.fragment_locate_tag_rfid, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        tagPatternLabel       = view.findViewById(R.id.tagPattern)
        relativeDistanceLabel = view.findViewById(R.id.relativeDistance)
        readerConnectionLabel = view.findViewById(R.id.readerConnectionLabel)
        distanceBar           = view.findViewById(R.id.distanceBar)
        distanceBarContainer  = view.findViewById(R.id.distanceBarContainer)
        hintLayout            = view.findViewById(R.id.hintLayout)

        val tagArg = arguments?.getString(ARG_TAG)
        val barcodeArg = arguments?.getString(ARG_BARCODE)

        Log.d(TAG, "onViewCreated: ARG_TAG=$tagArg, ARG_BARCODE=$barcodeArg")

        tagArg?.takeIf { it.isNotEmpty() }?.let {
            viewModel.tagPattern.value = it
        }
        barcodeArg?.takeIf { it.isNotEmpty() }?.let {
            viewModel.titleText.value = it
        }

        observeViewModel()
    }
    private fun observeViewModel() {
        viewModel.titleText.observe(viewLifecycleOwner) {
            tagPatternLabel.text = it
        }

        viewModel.relativeDistance.observe(viewLifecycleOwner) {
            relativeDistanceLabel.text = it
        }

        viewModel.readerConnection.observe(viewLifecycleOwner) {
            readerConnectionLabel.text = it
        }

        viewModel.distanceBox.observe(viewLifecycleOwner) { rect ->
            if (distanceBarContainer.height > 0) {
                updateDistanceBar(rect)
            } else {
                distanceBarContainer.post { updateDistanceBar(rect) }
            }
        }

        viewModel.hintAvailable.observe(viewLifecycleOwner) { visible ->
            hintLayout.visibility = if (visible) View.VISIBLE else View.GONE
        }
    }

    override fun onResume() {
        super.onResume()
        val rfidHandler = (requireActivity() as? MainActivity)?.getRfidHandler()
        rfidHandler?.enableRfidMode()
        viewModel.updateIn()
    }

    override fun onPause() {
        super.onPause()
        viewModel.updateOut()
    }

    override fun onDestroy() {
        super.onDestroy()
        viewModel.dispose()
    }

    private fun updateDistanceBar(rect: RectF) {
        val totalPx = distanceBarContainer.height
        if (totalPx == 0) {
            return
        }

        val fillRatio = rect.bottom.coerceIn(0f, 1f)
        val fillPx    = (fillRatio * totalPx).toInt()

        val params    = distanceBar.layoutParams
        params.height = fillPx
        distanceBar.layoutParams = params
    }
}