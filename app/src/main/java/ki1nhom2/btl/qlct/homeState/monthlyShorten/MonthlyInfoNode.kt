package ki1nhom2.btl.qlct.homeState.monthlyShorten

import ki1nhom2.btl.qlct.addState.addCost.ExpenditureCostNode

class MonthlyInfoNode(
    var monthName: String,
    var income: Long,
    var outcome: Long,
    var consumptionList : MutableList<ExpenditureCostNode> = mutableListOf()
)