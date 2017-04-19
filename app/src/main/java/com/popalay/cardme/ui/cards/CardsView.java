package com.popalay.cardme.ui.cards;

import com.arellomobile.mvp.viewstate.strategy.AddToEndSingleStrategy;
import com.arellomobile.mvp.viewstate.strategy.OneExecutionStateStrategy;
import com.arellomobile.mvp.viewstate.strategy.StateStrategyType;
import com.popalay.cardme.data.models.Card;
import com.popalay.cardme.ui.removablelistitem.RemovableListItemView;

import io.card.payment.CreditCard;

@StateStrategyType(AddToEndSingleStrategy.class)
public interface CardsView extends RemovableListItemView<Card> {

    @StateStrategyType(OneExecutionStateStrategy.class)
    void startCardScanning();

    @StateStrategyType(OneExecutionStateStrategy.class)
    void addCardDetails(CreditCard card);

    @StateStrategyType(OneExecutionStateStrategy.class)
    void shareCardNumber(String cardNumber);

}