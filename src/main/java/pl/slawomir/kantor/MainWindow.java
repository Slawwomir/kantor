package pl.slawomir.kantor;

import org.openqa.selenium.WebDriver;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Timer;
import java.util.TimerTask;

import static java.lang.Math.abs;

/**
 * Created by Sławomir on 09.09.2017.
 */
public class MainWindow extends JFrame implements ActionListener, ChangeListener {
    private WebDriver driver;
    private LoginAction session;
    private LoginFrame frame;
    private CurrencyPage currencies;
    private double extra;
    private boolean isRefreshing;
    private Timer t;

    private JSlider slider;
    private JComboBox currencyList;
    private JLabel currencySell;
    private JLabel currencyBuy;
    private JLabel buyExtra;
    private JLabel sellExtra;
    private JLabel sumBuy;
    private JLabel sumSell;
    private JRadioButton roundingSellRadio;
    private JRadioButton roundingBuyRadio;
    private JButton buyButton;
    private JButton sellButton;
    private JButton close;
    private JLabel baseAmountLabel;
    private JLabel quotedAmountLabel;
    private JTextField baseAmount;
    private JTextField quotedAmount;

    public MainWindow(LoginFrame frame){
        super("Kantor");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width/2, screenSize.height/2);
        setResizable(false);
        setLocationRelativeTo(null);

        extra = 0;

        this.frame = frame;
        this.driver = frame.driver;
        this.session = frame.getSession();
        currencies = new CurrencyPage(driver, session);

        createView();
        pack();
        setVisible(true);

        startRefreshing();
    }

    private void createView(){
        JPanel panel = new JPanel();
        this.add(panel);
        panel.setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();
        c.gridx = 0;
        c.gridy = 0;
        c.gridwidth = 2;
        c.anchor = GridBagConstraints.WEST;
        c.insets = new Insets(30,10,10,30);

        currencyList = new JComboBox(currencies.getCurrencyList());
        panel.add(currencyList, c);
        currencyList.addActionListener(this);
        /////////////////////////////////////////////////////////////////////////////

        c.gridwidth = 1;
        c.insets = new Insets(2,10,2,2);
        c.anchor = GridBagConstraints.WEST;
        String cur = currencyList.getSelectedItem().toString();

        c.gridy++;
        baseAmount = new JTextField(6);
        baseAmountLabel = new JLabel(cur.substring(0,3));
        panel.add(baseAmountLabel, c);

        c.gridx++;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(baseAmount, c);

        c.gridy++;
        c.gridx--;
        c.fill = GridBagConstraints.NONE;
        quotedAmount = new JTextField(6);
        quotedAmountLabel = new JLabel(cur.substring(cur.length() - 3,cur.length()));
        panel.add(quotedAmountLabel, c);

        c.gridx++;
        c.fill = GridBagConstraints.HORIZONTAL;
        panel.add(quotedAmount, c);

        ///////////////////////////////////////////////////////////////////////////////
        c.insets = new Insets(30,20,10,20);
        c.gridy = 0;
        c.anchor = GridBagConstraints.EAST;
        c.fill = GridBagConstraints.EAST;
        c.gridx++;
        JLabel sellLabel = new JLabel("Sprzedaż");
        panel.add(sellLabel, c);

        c.gridx++;
        JLabel buyLabel = new JLabel("Kupno");
        panel.add(buyLabel, c);

        c.insets = new Insets(5, 20, 5, 20);
        c.gridx--;
        c.gridy++;

        currencySell = new JLabel();
        panel.add(currencySell, c);

        c.gridy++;
        sellExtra = new JLabel();
        panel.add(sellExtra, c);

        c.gridy++;
        sumSell = new JLabel();
        panel.add(sumSell, c);

        c.gridx++;
        c.gridy -= 2;
        currencyBuy = new JLabel();
        panel.add(currencyBuy, c);

        c.gridy++;
        buyExtra = new JLabel();
        panel.add(buyExtra, c);

        c.gridy++;
        sumBuy = new JLabel();
        panel.add(sumBuy, c);

        c.insets = new Insets(30,20,10,20);

        c.gridx--;
        c.gridy++;
        sellButton = new JButton("Sprzedaję");
        sellButton.addActionListener(this);
        panel.add(sellButton, c);

        c.gridx++;
        buyButton = new JButton("Kupuję");
        buyButton.addActionListener(this);
        panel.add(buyButton, c);

        ButtonGroup radioGroup = new ButtonGroup();

        c.gridx = 0;
        c.gridy++;
        c.insets = new Insets(5,20,5,20);
        c.gridwidth = 3;
        c.anchor = GridBagConstraints.WEST;
        //Automatyczne dopełnianie do grosza
        roundingSellRadio = new JRadioButton("Zaokrąglij do grosza (sprzedaż)");
        roundingSellRadio.addActionListener(this);
        roundingSellRadio.setSelected(true);
        panel.add(roundingSellRadio, c);
        radioGroup.add(roundingSellRadio);

        c.gridy++;
        //Automatyczne dopełnianie do grosza
        roundingBuyRadio = new JRadioButton("Zaokrąglij do grosza (kupno)");
        roundingBuyRadio.addActionListener(this);
        roundingBuyRadio.setSelected(true);
        panel.add(roundingBuyRadio, c);
        radioGroup.add(roundingBuyRadio);

        c.gridy++;
        JRadioButton blank = new JRadioButton("Nie zaokrąglaj");
        blank.addActionListener(this);
        blank.setSelected(true);
        panel.add(blank, c);
        radioGroup.add(blank);

        c.insets = new Insets(10,20,30,20);
        c.gridx = 0;
        c.gridy++;
        c.gridwidth = 4;
        c.fill = GridBagConstraints.HORIZONTAL;
        slider = new JSlider(JSlider.HORIZONTAL);
        slider.addNotify();
        slider.setMaximum(1000);
        slider.setMinimum(0);
        slider.setValue(0);
        slider.addChangeListener(this);
        slider.setMajorTickSpacing(1000);
        slider.setMinorTickSpacing(100);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        panel.add(slider, c);

        c.gridy++;
        c.gridwidth = 2;
        c.fill = GridBagConstraints.NONE;
        c.insets = new Insets(10,10,20,10);
        close = new JButton("Zakończ");
        close.addActionListener(this);
        panel.add(close, c);

    }

    public void actionPerformed(ActionEvent e){
        Object source = e.getSource();
        if(source == currencyList) {

            if(!isRefreshing)
                startRefreshing();

            String cur = currencyList.getSelectedItem().toString();
            quotedAmountLabel.setText(cur.substring(cur.length() - 3,cur.length()));
            baseAmountLabel.setText(cur.substring(0,3));
            currencies.setCurrency(cur);
            refreshExchange();
        } else if(source == buyButton){
            doTransaction(TransactionPage.BUY);
        } else if(source == sellButton){
            doTransaction(TransactionPage.SELL);
        } else if(source == roundingBuyRadio || source == roundingSellRadio){
            refreshExchange();
        } else if(source == close){
            this.close();
        }
    }

    public  void stateChanged(ChangeEvent e){
        extra = (slider.getValue()/10000.0);
        refreshExchange();
    }

    private void doTransaction(final int index){
        stopRefreshing();
        final MainWindow mw = this;
        final Double sellP = Double.valueOf(currencySell.getText().replace(',','.'));
        final Double buyP = Double.valueOf(currencyBuy.getText().replace(',','.'));
        TransactionPage transaction = new TransactionPage(driver);
        transaction.setCurrency(currencyList.getSelectedItem().toString());
        transaction.setAction(index);

        new Thread(new Runnable() { //Buy = 0, Sell = 1
            public void run() {
                Double value;
                if(index == TransactionPage.BUY){
                    value = buyP;
                }else {
                    value = sellP;
                }
                
                Double ex = extra;
                String ch = " + ";

                if(baseAmount.getText().length() > 0){
                    Double amount = Double.valueOf(baseAmount.getText().replace(',', '.'));
                    Double sumNoExtra = amount*value;
                    Double margin = ex*amount;

                    if(index == TransactionPage.SELL) {
                        margin = -margin;
                        ex = -ex;
                        ch = " - ";
                    }
                    
                    JOptionPane.showMessageDialog(mw, baseAmountLabel.getText() + " -> " +
                            quotedAmountLabel.getText() + "\n\nNależność:\n" + amount.toString().replace('.',
                            ',') + " * (" + value.toString().replace('.',',') + ch +
                            String.format("%.4f",abs(ex)) + ") =\n= " + String.format("%.2f",sumNoExtra) + ch +
                            String.format("%.2f",abs(margin)) + " =\n= " + String.format("%.2f",(sumNoExtra + margin)));
                }else if(quotedAmount.getText().length() > 0){
                    Double amount = Double.valueOf(quotedAmount.getText().replace(',', '.'));
                    Double sumNoExtra = amount/value;
                    Double withMargin = amount/(value + ex);

                    if(index == TransactionPage.SELL) {
                        withMargin = amount / (value - ex);
                        ex = -ex;
                        ch = " - ";
                    }
                    
                    JOptionPane.showMessageDialog(mw, quotedAmountLabel.getText() + " -> " +
                            baseAmountLabel.getText() + "\n\nNależność:\n" + amount.toString().replace('.',
                            ',') + " / (" + value.toString().replace('.',',') + ch +
                            String.format("%.4f",abs(ex)) + ") =\n= " + String.format("%.2f",sumNoExtra) + ((sumNoExtra - withMargin > 0)?" - ":" + ") +
                            String.format("%.2f",abs(sumNoExtra - withMargin)) + " =\n= " + String.format("%.2f",(withMargin)));
                }
            }
        }).start();

        Double value;

        if(index == TransactionPage.BUY){
            value = buyP;
        }else {
            value = sellP;
        }

        Double ex = extra;

        if(quotedAmount.getText().length() > 0){

            Double amount = Double.valueOf(quotedAmount.getText().replace(',', '.'));
            Double withMargin;
            if(index == TransactionPage.BUY) {
                withMargin = amount / (value + ex);
                transaction.setBaseAmount(String.valueOf(withMargin));
            }else{
                withMargin = amount / (value - ex);
                transaction.setQuotedAmount(String.valueOf(withMargin * sellP));
            }
        }else{
            transaction.setQuotedAmount(quotedAmount.getText());
            transaction.setBaseAmount(baseAmount.getText());
        }
        transaction.realize();
    }

    private void refreshExchange(){
        currencySell.setText(String.format("%.4f", currencies.getExchangeSell()));
        currencyBuy.setText(String.format("%.4f", currencies.getExchangeBuy()));
        double sell = currencies.getExchangeSell() - extra;
        double buy = currencies.getExchangeBuy() + extra;

        if(roundingSellRadio.isSelected()){
            sell = rounding(sell);
            extra = -(sell - currencies.getExchangeSell());
            if(extra < 0){
                extra += 0.01;
                sell += 0.01;
            }
        }else if(roundingBuyRadio.isSelected()){
            buy = rounding(buy);
            extra = buy - currencies.getExchangeBuy();
            if(extra < 0){
                extra += 0.01;
                buy += 0.01;
            }
        }

        slider.setValue((int)(10000*extra));

        buyExtra.setText(String.format("+ %.4f", extra));
        sellExtra.setText(String.format("- %.4f", extra));
        sumSell.setText(String.format("%.4f", sell));
        sumBuy.setText(String.format("%.4f", buy));
    }

    private double rounding(double result){
        int prize = (int)(10000*result);
        if(prize % 100 >= 50){
            result = ((prize/100) + 1)/100.0;
        }else{
            result = (prize/100)/100.0;
        }
        return result;
    }

    private void startRefreshing(){
        t = new Timer();
        isRefreshing = true;
        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                refreshExchange();
            }
        }, 0, 500);
    }

    private void stopRefreshing(){
        isRefreshing = false;
        t.cancel();
    }

    public void close(){
        isRefreshing = false;
        t.cancel();
        driver.close();
        this.dispose();
    }
}
