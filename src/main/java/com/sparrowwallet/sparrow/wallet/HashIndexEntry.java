package com.sparrowwallet.sparrow.wallet;

import com.sparrowwallet.drongo.wallet.BlockTransaction;
import com.sparrowwallet.drongo.wallet.BlockTransactionHashIndex;
import com.sparrowwallet.drongo.wallet.Wallet;
import com.sparrowwallet.sparrow.EventManager;
import com.sparrowwallet.sparrow.control.DateLabel;
import com.sparrowwallet.sparrow.event.WalletChangedEvent;

import java.util.Collections;
import java.util.List;

public class HashIndexEntry extends Entry {
    private final Wallet wallet;
    private final BlockTransactionHashIndex hashIndex;
    private final Type type;

    public HashIndexEntry(Wallet wallet, BlockTransactionHashIndex hashIndex, Type type) {
        super(hashIndex.getLabel(), hashIndex.getSpentBy() != null ? List.of(new HashIndexEntry(wallet, hashIndex.getSpentBy(), Type.INPUT)) : Collections.emptyList());
        this.wallet = wallet;
        this.hashIndex = hashIndex;
        this.type = type;

        labelProperty().addListener((observable, oldValue, newValue) -> {
            hashIndex.setLabel(newValue);
            EventManager.get().post(new WalletChangedEvent(wallet));
        });
    }

    public Wallet getWallet() {
        return wallet;
    }

    public BlockTransactionHashIndex getHashIndex() {
        return hashIndex;
    }

    public Type getType() {
        return type;
    }

    public BlockTransaction getBlockTransaction() {
        return wallet.getTransactions().get(hashIndex.getHash());
    }

    public String getDescription() {
        return (type.equals(Type.INPUT) ? "Spent by " : "Received from ") +
                getHashIndex().getHash().toString().substring(0, 8) + "...:" + getHashIndex().getIndex() +
                " on " + DateLabel.getShortDateFormat(getHashIndex().getDate());
    }

    public boolean isSpent() {
        return getType().equals(HashIndexEntry.Type.INPUT) || getHashIndex().getSpentBy() != null;
    }

    @Override
    public Long getValue() {
        return hashIndex.getValue();
    }

    public enum Type {
        INPUT, OUTPUT
    }
}