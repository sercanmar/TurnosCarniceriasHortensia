import { View, Text, Alert, TouchableOpacity, Image, TextInput } from 'react-native';
import { useTurnosStore } from '../presentation/store/useTurnosStore';
import { ThemedButton } from '../presentation/theme/components/ThemedButton';
import { useServidor } from '../hooks/useServidor';
import { useEffect, useState } from 'react';

export default function HomeScreen() {
  const { miTurno, turnoActual, cancelarTurno } = useTurnosStore();
  const { conectar, solicitarTurno } = useServidor();
  
  const [nombre, setNombre] = useState('');

  useEffect(() => {
    conectar((datos) => {
      if (datos.accion === 'ACTUALIZAR_PANTALLA') {
        useTurnosStore.setState({ turnoActual: datos.ticket });
      } else if (datos.accion === 'TURNO_ASIGNADO') {
        useTurnosStore.setState({ miTurno: datos.ticket });
      }
    });
  }, []);

  const tieneTurno = miTurno !== 'Sin turno';

  const confirmarCancelacion = () => {
    Alert.alert(
      "cancelar cita",
      " seguro que quieres cancelar tu cita?",
      [
        { text: "no", style: "cancel" },
        { text: "s , cancelar", onPress: cancelarTurno, style: "destructive" }
      ]
    );
  };
  const calcularMinutos = () => {
    if (!tieneTurno || turnoActual === 'T-0' || miTurno === 'Sin turno') return 0;
    try {
      const actual = parseInt(turnoActual.replace('T-', ''));
      const mio = parseInt(miTurno.replace('T-', ''));
      
      const diferencia = mio - actual;
      
      if (diferencia <= 0) return 0;
      
      return diferencia * 10;
    } catch (e) {
      return 0;
    }
  };

  const minutosEspera = calcularMinutos();
  return (
    <View className="flex-1 bg-stone-50 items-center justify-center p-6">
      
      <Image
        source={require('../assets/images/logo.png')}
        className="w-48 h-48 rounded-full mb-12 border-4 border-emerald-700 shadow-xl"
        resizeMode="cover"
      />

      {/* turno actuial */}
      <View className="bg-white p-8 rounded-3xl w-full items-center mb-6 shadow-sm border border-gray-100">
        <Text className="text-xl text-gray-500 mb-2 font-medium">Atendiendo ahora:</Text>
        <Text className="text-8xl font-black text-gray-900">{turnoActual}</Text>
      </View>

      {/* turno cliente */}
      <View className="bg-white p-8 rounded-3xl w-full items-center mb-10 shadow-sm border border-gray-100">
        <Text className="text-xl text-gray-500 mb-2 font-medium">Tu ticket:</Text>
        <Text className="text-8xl font-black text-emerald-700">{miTurno}</Text>
      {tieneTurno && minutosEspera > 0 && (
          <Text className="text-lg text-orange-500 mt-4 font-bold">
            tiempo estimado: {minutosEspera} min
          </Text>
        )}

      </View>

      {/* condicional btones */}
      {!tieneTurno ? (
        <View className="w-full">
          <TextInput
            placeholder="escribe tu nombre completo"
            value={nombre}
            onChangeText={setNombre}
            className="bg-white p-5 rounded-2xl mb-4 border border-emerald-200 text-lg shadow-sm text-gray-800"
          />
          <ThemedButton
            label="PEDIR CITA"
            className="w-full shadow-lg bg-emerald-700"
            onPress={() => solicitarTurno(nombre)}
          />
        </View>
      ) : (
        <TouchableOpacity
          className="bg-gray-400 py-5 px-10 rounded-2xl items-center w-full mt-4 active:bg-gray-500 shadow-md"
          onPress={confirmarCancelacion}
        >
          <Text className="text-white text-2xl font-bold">CANCELAR CITA</Text>
        </TouchableOpacity>
      )}
    </View>
  );
}