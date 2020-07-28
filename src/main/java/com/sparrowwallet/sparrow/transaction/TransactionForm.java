package com.sparrowwallet.sparrow.transaction;

import com.sparrowwallet.drongo.protocol.Sha256Hash;
import com.sparrowwallet.drongo.protocol.Transaction;
import com.sparrowwallet.drongo.psbt.PSBT;
import com.sparrowwallet.drongo.wallet.BlockTransaction;
import com.sparrowwallet.drongo.wallet.Keystore;
import com.sparrowwallet.drongo.wallet.Wallet;
import com.sparrowwallet.sparrow.io.Storage;
import javafx.beans.property.SimpleObjectProperty;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public abstract class TransactionForm {
    protected final TransactionData txdata;

    public TransactionForm(TransactionData txdata) {
        this.txdata = txdata;
    }

    public Transaction getTransaction() {
        return txdata.getTransaction();
    }

    public String getName() {
        return txdata.getName();
    }

    public PSBT getPsbt() {
        return txdata.getPsbt();
    }

    public BlockTransaction getBlockTransaction() {
        return txdata.getBlockTransaction();
    }

    public Map<Sha256Hash, BlockTransaction> getInputTransactions() {
        return txdata.getInputTransactions();
    }

    public int getMaxInputFetched() {
        return txdata.getMaxInputFetched();
    }

    public boolean allInputsFetched() {
        return txdata.allInputsFetched();
    }

    public List<BlockTransaction> getOutputTransactions() {
        return txdata.getOutputTransactions();
    }

    public int getMaxOutputFetched() {
        return txdata.getMaxOutputFetched();
    }

    public boolean allOutputsFetched() {
        return txdata.allOutputsFetched();
    }

    public ObservableMap<Wallet, Storage> getAvailableWallets() {
        return txdata.getAvailableWallets();
    }

    public Wallet getSigningWallet() {
        return txdata.getSigningWallet();
    }

    public SimpleObjectProperty<Wallet> signingWalletProperty() {
        return txdata.signingWalletProperty();
    }

    public void setSigningWallet(Wallet signingWallet) {
        txdata.setSigningWallet(signingWallet);
    }

    public ObservableList<Keystore> getSignedKeystores() {
        return txdata.getSignedKeystores();
    }

    public boolean isEditable() {
        if(getBlockTransaction() != null) {
            return false;
        }

        if(getPsbt() != null) {
            if(getPsbt().hasSignatures() || getPsbt().isSigned()) {
                return false;
            }
            return txdata.getSigningWallet() == null;
        }

        return true;
    }

    public abstract Node getContents() throws IOException;

    public abstract TransactionView getView();
}
