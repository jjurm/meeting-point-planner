package com.jjurm.projects.mpp.system;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import com.jjurm.projects.mpp.algorithm.Algorithm;
import com.jjurm.projects.mpp.algorithm.Algorithm.Result;
import com.jjurm.projects.mpp.algorithm.DiscreteAlgorithm;
import com.jjurm.projects.mpp.db.DatabaseManager;
import com.jjurm.projects.mpp.db.PlaceFinder;
import com.jjurm.projects.mpp.db.PlaceFinder.NotFoundException;
import com.jjurm.projects.mpp.model.Attendant;
import com.jjurm.projects.mpp.model.Place;

public class Application {

  ExecutorService executor = Executors.newSingleThreadExecutor();

  DefaultListModel<Attendant> attendants = new DefaultListModel<Attendant>();
  DefaultTableModel results = new DefaultTableModel(Result.tableColumns, 0);
  Algorithm algorithm;

  private JFrame frame;
  private JTextField textDate;
  private JTextField textOrigin;
  private JTextField textAge;
  private JTable tableResults;
  private JProgressBar progressBar;

  /**
   * Launch the application.
   */
  public static void main(String[] args) {
    DatabaseManager.init();

    EventQueue.invokeLater(new Runnable() {
      @Override
      public void run() {
        try {
          Application window = new Application();
          window.frame.setVisible(true);
          window.frame.setTitle("Meeting point planner");
          window.textOrigin.requestFocus();
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    });
  }

  /**
   * Create the application.
   */
  public Application() {
    initialize();
  }

  /**
   * Initialize the contents of the frame.
   */
  private void initialize() {
    frame = new JFrame();
    frame.setBounds(100, 100, 522, 322);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JPanel panelMain = new JPanel();
    frame.getContentPane().add(panelMain, BorderLayout.CENTER);
    panelMain.setLayout(null);

    JPanel panelInput = new JPanel();
    panelInput.setBounds(10, 11, 194, 262);
    panelInput.setBorder(BorderFactory.createTitledBorder("Input"));
    panelMain.add(panelInput);
    panelInput.setLayout(null);

    JLabel lblDate = new JLabel("Date:");
    lblDate.setBounds(10, 21, 46, 14);
    panelInput.add(lblDate);

    textDate = new JTextField();
    textDate.setText("20170412");
    textDate.setBounds(66, 18, 114, 20);
    panelInput.add(textDate);
    textDate.setColumns(10);

    JLabel lblAttendants = new JLabel("Attendants:");
    lblAttendants.setBounds(10, 46, 78, 14);
    panelInput.add(lblAttendants);

    JList<Attendant> list = new JList<Attendant>();
    list.setModel(attendants);
    list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JScrollPane scrollAttendants = new JScrollPane(list);
    scrollAttendants.setBounds(10, 71, 170, 94);
    panelInput.add(scrollAttendants);

    JLabel lblOrigin = new JLabel("Origin:");
    lblOrigin.setBounds(10, 176, 46, 14);
    panelInput.add(lblOrigin);

    textOrigin = new JTextField();
    textOrigin.setBounds(66, 173, 114, 20);
    panelInput.add(textOrigin);
    textOrigin.setColumns(10);

    JLabel lblAge = new JLabel("Age:");
    lblAge.setBounds(10, 201, 46, 14);
    panelInput.add(lblAge);

    textAge = new JTextField();
    textAge.setBounds(66, 198, 114, 20);
    textAge.addActionListener(this::addAttendant);
    panelInput.add(textAge);
    textAge.setColumns(10);

    JButton btnAdd = new JButton("Add");
    btnAdd.addActionListener(this::addAttendant);
    btnAdd.setBounds(10, 226, 78, 23);
    panelInput.add(btnAdd);

    JButton btnRemove = new JButton("Remove");
    btnRemove.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        int selectedIndex = list.getSelectedIndex();
        if (selectedIndex != -1) {
          attendants.remove(selectedIndex);
        }
      }
    });
    btnRemove.setBounds(94, 226, 86, 23);
    panelInput.add(btnRemove);

    algorithm = new DiscreteAlgorithm(10, d -> progressBar.setValue((int) (d * 1000)));

    JButton btnCalculate = new JButton("Compute");
    btnCalculate.addActionListener(e -> executor.submit(this::compute));
    btnCalculate.setBounds(214, 11, 89, 23);
    panelMain.add(btnCalculate);

    tableResults = new JTable();
    tableResults.setModel(results);

    JScrollPane scrollResults = new JScrollPane(tableResults);
    scrollResults.setBounds(214, 45, 282, 228);
    panelMain.add(scrollResults);

    progressBar = new JProgressBar();
    progressBar.setBounds(313, 20, 183, 14);
    progressBar.setMinimum(0);
    progressBar.setMaximum(1000);
    panelMain.add(progressBar);

  }

  private void addAttendant(ActionEvent e) {
    try {
      Place origin = PlaceFinder.city(textOrigin.getText());
      double age = Double.parseDouble(textAge.getText());
      Attendant at = new Attendant(origin, age);
      attendants.addElement(at);
      textOrigin.setText("");
      textAge.setText("");
    } catch (NotFoundException e1) {
      // do nothing
    } catch (SQLException e1) {
      e1.printStackTrace();
    }
    textOrigin.requestFocus();
  }

  private void compute() {
    try {
      SimpleDateFormat parser = new SimpleDateFormat("yyyyMMdd");
      Date date = parser.parse(textDate.getText());
      ArrayList<Attendant> ats = Collections.list(attendants.elements());

      TreeSet<Result> resultSet = algorithm.find(date, ats.toArray(new Attendant[0]));
      while (results.getRowCount() > 0)
        results.removeRow(0);
      for (Result r : resultSet) {
        results.addRow(r.getTableRow());
      }
    } catch (ParseException e1) {
      e1.printStackTrace();
    }
  }
}
