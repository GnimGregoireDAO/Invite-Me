package com.inviteme.ui

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.inviteme.databinding.ItemEventBinding
import com.inviteme.model.entities.Evenement
import com.inviteme.model.entities.Lieu
import com.inviteme.model.repos.LieuRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class EvenementAdapter(
    private var evenements: List<Evenement>,
    private val onEdit: (Evenement) -> Unit,
    private val onDelete: (Evenement) -> Unit,
    private val lieuRepository: LieuRepository,
    private val coroutineScope: CoroutineScope
) : RecyclerView.Adapter<EvenementAdapter.EvenementViewHolder>() {
    
    inner class EvenementViewHolder(val binding: ItemEventBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(evenement: Evenement) {
            binding.evenement = evenement
            
            // Fetch and bind Lieu information
            evenement.lieuId?.let {
                coroutineScope.launch {
                    val lieu = withContext(Dispatchers.IO) {
                        lieuRepository.getLieuById(it.toInt())
                    }
                    binding.lieu = lieu
                    binding.executePendingBindings() // Re-bind after lieu is fetched
                }
            } ?: run {
                binding.lieu = null // No lieu associated
                binding.executePendingBindings()
            }
            // Branche les boutons déjà présents dans le layout
            binding.Edit.setOnClickListener { onEdit(evenement) }
            binding.Delete.setOnClickListener { onDelete(evenement) }

            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, VoirEvenementActivity::class.java)

                intent.putExtra("evenementId", evenement.id)

                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EvenementViewHolder {
        val binding = ItemEventBinding.inflate(
            LayoutInflater.from(parent.context), parent, false)
        return EvenementViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EvenementViewHolder, position: Int) {
        holder.bind(evenements[position])
    }
    
    override fun getItemCount(): Int = evenements.size

    fun updateEvents(newEvenements: List<Evenement>) {
        this.evenements = newEvenements
        notifyDataSetChanged()
    }
}
