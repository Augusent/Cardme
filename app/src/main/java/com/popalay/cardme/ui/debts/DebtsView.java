package com.popalay.cardme.ui.debts;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.data.models.Debt;
import com.popalay.cardme.ui.removablelistitem.RemovableListItemView;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface DebtsView extends RemovableListItemView<Debt> {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void showAddDialog();
}