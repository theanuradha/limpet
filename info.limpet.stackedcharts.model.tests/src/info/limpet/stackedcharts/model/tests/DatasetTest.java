/**
 */
package info.limpet.stackedcharts.model.tests;

import info.limpet.stackedcharts.model.Dataset;
import info.limpet.stackedcharts.model.StackedchartsFactory;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Dataset</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class DatasetTest extends TestCase
{

  /**
   * The fixture for this Dataset test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Dataset fixture = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(DatasetTest.class);
  }

  /**
   * Constructs a new Dataset test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DatasetTest(String name)
  {
    super(name);
  }

  /**
   * Sets the fixture for this Dataset test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void setFixture(Dataset fixture)
  {
    this.fixture = fixture;
  }

  /**
   * Returns the fixture for this Dataset test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Dataset getFixture()
  {
    return fixture;
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see junit.framework.TestCase#setUp()
   * @generated
   */
  @Override
  protected void setUp() throws Exception
  {
    setFixture(StackedchartsFactory.eINSTANCE.createDataset());
  }

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @see junit.framework.TestCase#tearDown()
   * @generated
   */
  @Override
  protected void tearDown() throws Exception
  {
    setFixture(null);
  }

} //DatasetTest
