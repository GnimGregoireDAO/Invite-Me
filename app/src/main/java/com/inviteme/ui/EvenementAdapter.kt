package com.inviteme.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.inviteme.databinding.ItemEventBinding
import com.inviteme.model.entities.Evenement

class EvenementAdapter(private var evenements: List<Evenement>) : 
    RecyclerView.Adapter<EvenementAdapter.EvenementViewHolder>() {
    
    fun updateEvents(newEvenements: List<Evenement>) {
        this.evenements = newEvenements
        notifyDataSetChanged()
    }
    
    inner class EvenementViewHolder(val binding: ItemEventBinding) : 
        RecyclerView.ViewHolder(binding.root) {
        
        fun bind(evenement: Evenement) {
            // Assigner l'événement au binding pour utiliser le data binding avec @{} dans le XML
            binding.evenement = evenement
            
            binding.executePendingBindings()
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
}
