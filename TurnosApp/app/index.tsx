import { View, Text, Alert, TouchableOpacity, Image } from 'react-native';
import { useTurnosStore } from '../presentation/store/useTurnosStore';
import { ThemedButton } from '../presentation/theme/components/ThemedButton';

export default function HomeScreen() {
  const { miTurno, turnoActual, pedirTurno, cancelarTurno } = useTurnosStore();

  const tieneTurno = miTurno !== 'Sin turno';

  const confirmarCancelacion = () => {
    Alert.alert(
      "cancelar turno",
      "¿seguro que quieres cancelar tu turno?",
      [
        { text: "no", style: "cancel" },

        { text: "sí, cancelar", onPress: cancelarTurno, style: "destructive" }
      ]
    );
  };

  return (
    <View className="flex-1 bg-stone-50 items-center justify-center p-6">
      
      <Image 
        source={require('../assets/images/logo.png')} 
        className="w-48 h-48 rounded-full mb-12 border-4 border-red-900 shadow-xl"
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
        <Text className="text-8xl font-black text-green-700">{miTurno}</Text>
      </View>

      {/* condicional btones */}
      {!tieneTurno ? (
        <ThemedButton 
          label="SACAR TURNO" 
          className="w-full mt-4 shadow-lg bg-red-900"
          onPress={pedirTurno} 
        />
      ) : (
        <TouchableOpacity 
          className="bg-gray-400 py-5 px-10 rounded-2xl items-center w-full mt-4 active:bg-gray-500 shadow-md"
          onPress={confirmarCancelacion}
        >
          <Text className="text-white text-2xl font-bold">CANCELAR TURNO</Text>
        </TouchableOpacity>
      )}

    </View>
  );
}