package com.inviteme.states

import java.sql.Timestamp

data class EvenmentState(
    var titre: String? = null,
    var description: String? = null,
    var type: String? = null,
    var lieu: Int? = null,
    var date: Timestamp? = null,
    var dateModification: Timestamp? = null
)