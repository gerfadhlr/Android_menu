package com.example.androidmenu

import android.content.Intent
import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context

class MainActivity : AppCompatActivity() {

    private lateinit var tvQuote: TextView
    private lateinit var btnPopup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        title = "Latihan Menu Android"

        tvQuote = findViewById(R.id.tvQuote)
        btnPopup = findViewById(R.id.btnPopup)

        // Context Menu: daftarkan view
        registerForContextMenu(tvQuote)

        // Popup Menu: tampil saat tombol diklik
        btnPopup.setOnClickListener { showPopupMenu(it) }
    }

    // ========== OPTIONS MENU ==========
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_search -> { showSearchDialog(); true }
            R.id.action_settings -> { toast("Menu Settings dipilih"); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun showSearchDialog() {
        val input = EditText(this).apply { hint = "Ketik kata kunci" }
        AlertDialog.Builder(this)
            .setTitle("Search")
            .setView(input)
            .setNegativeButton("BATAL", null)
            .setPositiveButton("CARI") { _, _ ->
                toast("Mencari: ${input.text}")
            }
            .show()
    }

    // ========== CONTEXT MENU ==========
    override fun onCreateContextMenu(
        menu: ContextMenu,
        v: View,
        menuInfo: ContextMenu.ContextMenuInfo?
    ) {
        super.onCreateContextMenu(menu, v, menuInfo)
        menu.setHeaderTitle("Pilih Tindakan")
        menuInflater.inflate(R.menu.menu_context, menu)
    }

    override fun onContextItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.ctx_copy -> {
                val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                cm.setPrimaryClip(ClipData.newPlainText("teks", tvQuote.text))
                toast("Teks disalin")
                true
            }
            R.id.ctx_edit -> { showEditDialog(); true }
            else -> super.onContextItemSelected(item)
        }
    }

    private fun showEditDialog() {
        val input = EditText(this).apply { setText(tvQuote.text) }
        AlertDialog.Builder(this)
            .setTitle("Edit Teks")
            .setView(input)
            .setNegativeButton("BATAL", null)
            .setPositiveButton("SIMPAN") { _, _ ->
                tvQuote.text = input.text.toString()
                toast("Teks berhasil diperbarui")
            }
            .show()
    }

    // ========== POPUP MENU ==========
    private fun showPopupMenu(anchor: View) {
        val popup = PopupMenu(this, anchor)
        popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)
        popup.setOnMenuItemClickListener { item ->
            when (item.itemId) {
                R.id.pop_download -> { toast("Download dipilih"); true }
                R.id.pop_share -> { shareText(tvQuote.text.toString()); true }
                R.id.pop_action -> { toast("Action dipilih"); true }
                else -> false
            }
        }
        popup.show()
    }

    // ========== FUNGSI BANTUAN ==========
    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()

    private fun shareText(text: String) {
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        startActivity(Intent.createChooser(intent, "Bagikan melalui"))
    }
}