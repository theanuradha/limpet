/**
 */
package info.limpet.stackedcharts.model.tests;

import info.limpet.stackedcharts.model.Datum;
import info.limpet.stackedcharts.model.StackedchartsFactory;

import junit.framework.TestCase;

import junit.textui.TestRunner;

/**
 * <!-- begin-user-doc -->
 * A test case for the model object '<em><b>Datum</b></em>'.
 * <!-- end-user-doc -->
 * @generated
 */
public class DatumTest extends TestCase
{

  /**
   * The fixture for this Datum test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Datum fixture = null;

  /**
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public static void main(String[] args)
  {
    TestRunner.run(DatumTest.class);
  }

  /**
   * Constructs a new Datum test case with the given name.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  public DatumTest(String name)
  {
    super(name);
  }

  /**
   * Sets the fixture for this Datum test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected void setFixture(Datum fixture)
  {
    this.fixture = fixture;
  }

  /**
   * Returns the fixture for this Datum test case.
   * <!-- begin-user-doc -->
   * <!-- end-user-doc -->
   * @generated
   */
  protected Datum getFixture()
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
    setFixture(StackedchartsFactory.eINSTANCE.createDatum());
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

} //DatumTest
